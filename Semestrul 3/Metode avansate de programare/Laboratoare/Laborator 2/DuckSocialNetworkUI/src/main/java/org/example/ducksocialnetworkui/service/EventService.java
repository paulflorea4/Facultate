package org.example.ducksocialnetworkui.service;

import javafx.application.Platform;
import org.example.ducksocialnetworkui.domain.SwimmingDuck;
import org.example.ducksocialnetworkui.domain.User;
import org.example.ducksocialnetworkui.event.*;
import org.example.ducksocialnetworkui.exception.ServiceException;
import org.example.ducksocialnetworkui.observer.Observable;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.repository.EventRepository;
import org.example.ducksocialnetworkui.repository.UserRepository;
import org.example.ducksocialnetworkui.validation.ValidatorEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventService implements Observable<EntityChangeEvent<Event>> {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final List<Observer<EntityChangeEvent<Event>>> observers = new ArrayList<>();

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final ValidatorEvent validatorEvent;

    public EventService(EventRepository eventRepository, UserRepository userRepository, ValidatorEvent validatorEvent) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.validatorEvent = validatorEvent;
    }

    public void addRaceEvent(String name,List<Long> distances) {
        if (distances.isEmpty())
            throw new ServiceException("Trebuie sa existe cel putin o distanta.");


        RaceEvent event = new RaceEvent(null, name, "PENDING" ,distances);
        validatorEvent.validate(event);

        Optional<Event> savedEvent = eventRepository.save(event);
        savedEvent.ifPresent(m ->notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.ADD,m)));
    }

    public void removeRaceEvent(long id) {
        Optional<Event> removedEvent = eventRepository.delete(id);
        if(removedEvent.isEmpty())
            throw new ServiceException("Event not found.");
        removedEvent.ifPresent(m ->
                notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.DELETE, null, m)));
    }

    public void addDuckToRace(Long eventId,Long duckId){ eventRepository.saveDuckToRace(eventId,duckId); }

    public void removeDuckFromRace(Long eventId,Long duckId){eventRepository.deleteDuckFromRace(eventId,duckId); }


    private List<SwimmingDuck> getAllSwimmingDucks() {
        var users = userRepository.findAll();
        List<SwimmingDuck> swimmingDucks = new ArrayList<>();
        for (var user : users) {
            System.out.println(user);
            if (user instanceof SwimmingDuck d) {
                swimmingDucks.add(d);
            }
        }
        return swimmingDucks;
    }

    public Event getEvent(long id) {
        return eventRepository.findOne(id)
                .orElseThrow(() -> new ServiceException("Nu exista eveniment cu id " + id));
    }

    public void startRaceEvent(Long id){
        Optional<Event> event = eventRepository.findOne(id);
        if(event.isEmpty())
            throw new ServiceException("Event not found.");
        RaceEvent race = (RaceEvent) event.get();

        if(race.getDucks().size()<race.getDistances().size())
            throw new ServiceException("Nu exista destule rate pentru eveniment!");

        race.setSubscribers(getSubscribers(race.getEventID()).size());
        CompletableFuture
                .supplyAsync(() -> {
                    race.startEvent();
                    return race.getResults();
                }, executor)
                .thenAcceptAsync(results -> {
                    eventRepository.updateStatus(race.getEventID(),"FINISHED");
                    eventRepository.saveResults(race.getEventID(), results);
                    notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.EVENT_FINISHED, race));
                }, Platform::runLater)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    public boolean isUserSubscribed(Long eventID, Long userID){
        return eventRepository.findIfUserIsSubscribed(eventID, userID);
    }

    public void subscribe(long eventId, long userId) {
        Event ev = getEvent(eventId);

        User u = userRepository.findOne(userId)
                .orElseThrow(() -> new ServiceException("Nu exista user cu id " + userId));

        List<Long> subs = eventRepository.getSubscribers(eventId);
        if (subs.contains(userId))
            throw new ServiceException("Utilizatorul este deja abonat la acest eveniment.");

        eventRepository.addSubscriber(eventId, userId);
    }

    public void unsubscribe(long eventId, long userId) {
        getEvent(eventId);

        userRepository.findOne(userId)
                .orElseThrow(() -> new ServiceException("Nu exista user cu id " + userId));

        List<Long> subs = eventRepository.getSubscribers(eventId);
        if (!subs.contains(userId))
            throw new ServiceException("Utilizatorul nu este abonat la acest eveniment.");

        eventRepository.removeSubscriber(eventId, userId);
    }

    public List<User> getSubscribers(long eventId) {
        getEvent(eventId);

        List<Long> ids = eventRepository.getSubscribers(eventId);
        List<User> users = new ArrayList<>();

        for (Long id : ids) {
            userRepository.findOne(id).ifPresent(users::add);
        }

        return users;
    }

    public List<Event> getAllEvents() {
        List<Event> list = new ArrayList<>();
        eventRepository.findAll().forEach(list::add);
        return list;
    }

    public List<Event> getEventsByStatus(String status){
        return eventRepository.findEventsByStatus(status);
    }

    public String getRaceResults(Long eventID) {
        return eventRepository.findResultsOfRace(eventID);
    }

    @Override
    public void addObserver(Observer<EntityChangeEvent<Event>> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent<Event>> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(EntityChangeEvent<Event> e) {
        observers.forEach(observer -> observer.update(e));
    }
}

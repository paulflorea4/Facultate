package org.example.laboratorGUI.service;

import javafx.application.Platform;
import org.example.laboratorGUI.domain.user.User;
import org.example.laboratorGUI.events.Event;
import org.example.laboratorGUI.events.RaceEvent;
import org.example.laboratorGUI.observer.Observable;
import org.example.laboratorGUI.repository.database.DBEventRepository;
import org.example.laboratorGUI.repository.database.DBUserRepository;
import org.example.laboratorGUI.exceptions.ServiceException;
import org.example.laboratorGUI.utils.event.EntityChangeEvent;
import org.example.laboratorGUI.utils.event.EntityChangeEventType;
import org.example.laboratorGUI.utils.types.TipEvent;
import org.example.laboratorGUI.validators.EventValidator;
import org.example.laboratorGUI.validators.Validator;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventService extends Observable {
    private final DBUserRepository userRepo;
    private final DBEventRepository eventsRepo;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final Validator<Event> eventValidator = new EventValidator();

    public EventService(DBUserRepository userRepo, DBEventRepository eventsRepo) {
        this.userRepo = userRepo;
        this.eventsRepo = eventsRepo;
    }

    /**
     * Starts a new race event based on the problem 'Natatie'
     *
     * @param name the name of the event
     * @param type the type of the event
     * @param distances at what distances are the buoys placed
     */
    public void addRaceEvent(String name, TipEvent type, List<Long> distances){
        RaceEvent event = new RaceEvent(1L, name, type, "PENDING", distances);
        eventValidator.validate(event);
        eventsRepo.save(event);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.EVENT_ADDED, event));
    }

    public void addDuckToRaceEvent(Long eventID, Long duckID) {
        eventsRepo.saveDuckToRace(eventID, duckID);
    }

    public void removeDuckFromRaceEvent(Long eventID, Long duckID) {
        eventsRepo.deleteDuckFromRace(eventID, duckID);
    }

    public void removeRaceEvent(Long eventID){
        Event event = eventsRepo.findById(eventID);
        if (event == null)
            throw new ServiceException("Event not found!");
        eventsRepo.delete(eventID);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.EVENT_REMOVED, event));
    }

    public void startRaceEvent(Long eventID){
        RaceEvent race = (RaceEvent) eventsRepo.findById(eventID);
        race.setSubscribers(getSubscribersForAnEvent(race.getEventID()).size());
        CompletableFuture
                .supplyAsync(() -> {
                    race.startRace();
                    return race.showResults();
                }, executor)
                .thenAcceptAsync(results -> {
                    eventsRepo.updateRaceAsFinished(race.getEventID());
                    eventsRepo.saveResultsOfRace(race.getEventID(), results);
                    notifyObservers(new EntityChangeEvent(EntityChangeEventType.EVENT_FINISHED, race));
                }, Platform::runLater)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    public RaceEvent getRaceEvent(Long eventID){
        RaceEvent race = (RaceEvent) eventsRepo.findById(eventID);
        race.setSubscribers(getSubscribersForAnEvent(race.getEventID()).size());
        return race;
    }

    public String getRaceResults(Long eventID) {
        return eventsRepo.findResultsOfRace(eventID);
    }

    public List<Event> getEventsByStatus(String status){
        return eventsRepo.findEventsByStatus(status);
    }

    public void addSubscriber(Long eventID, Long userID){
        Event event = eventsRepo.findById(eventID);
        if (event == null)
            throw new ServiceException("Event not found!");
        User user = userRepo.findById(userID);
        if (user == null)
            throw new ServiceException("User not found!");
        eventsRepo.saveSubscriberToEvent(eventID, userID);
    }

    public void removeSubscriber(Long eventID, Long userID){
        Event event = eventsRepo.findById(eventID);
        if (event == null)
            throw new ServiceException("Event not found!");
        User user = userRepo.findById(userID);
        if (user == null)
            throw new ServiceException("User not found!");
       eventsRepo.deleteSubscriberFromEvent(eventID, userID);
    }

    public List<Long> getSubscribersForAnEvent(Long eventID){
        return eventsRepo.findSubscriberIDsForAnEvent(eventID);
    }

    public boolean isUserSubscribed(Long eventID, Long userID){
        return eventsRepo.findIfUserIsSubscribed(eventID, userID);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
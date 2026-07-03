package org.example.ducksocialnetworkui.service;

import org.example.ducksocialnetworkui.domain.Duck;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.EntityChangeEventType;
import org.example.ducksocialnetworkui.factory.Factory;
import org.example.ducksocialnetworkui.factory.FlockFactory;
import org.example.ducksocialnetworkui.flock.Flock;
import org.example.ducksocialnetworkui.observer.Observable;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.repository.FlockRepository;
import org.example.ducksocialnetworkui.utils.paging.Page;
import org.example.ducksocialnetworkui.utils.paging.Pageable;
import org.example.ducksocialnetworkui.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlockService implements Observable<EntityChangeEvent<Flock<? extends Duck>>> {
    private final FlockRepository flockRepository;
    private final Validator<Flock<? extends Duck>> validator;
    private final List<Observer<EntityChangeEvent<Flock<? extends Duck>>>> observers = new ArrayList<>();

    private final Factory<Flock<? extends Duck>> flockFactory=FlockFactory.getInstance();

    public FlockService(FlockRepository flockRepository, Validator<Flock<? extends Duck>> validator) {
        this.flockRepository = flockRepository;
        this.validator = validator;
    }

    public Optional<Flock<? extends Duck>> addFlock(String type,String name){
        Flock<? extends Duck> flock=flockFactory.create(type,null,name);
        validator.validate(flock);
        Optional<Flock<? extends Duck>> savedFlock=flockRepository.save(flock);
        savedFlock.ifPresent(m ->
                notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.ADD,m)));
        return savedFlock;
    }

    public Optional<Flock<? extends Duck>> removeFlock(Long id){
        Optional<Flock<? extends Duck>> removedFlock=flockRepository.delete(id);
        removedFlock.ifPresent(m->
                notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.DELETE,null,m)));
        return removedFlock;
    }

    public Iterable<Flock<? extends Duck>> getAllFlocks(){ return flockRepository.findAll(); }

    public Optional<Flock<? extends Duck>> getFlock(Long id){ return flockRepository.findOne(id); }

    @Override
    public void addObserver(Observer<EntityChangeEvent<Flock<? extends Duck>>> o) { observers.add(o); }

    @Override
    public void removeObserver(Observer<EntityChangeEvent<Flock<? extends Duck>>> o) { observers.remove(o); }

    @Override
    public void notifyObservers(EntityChangeEvent<Flock<? extends Duck>> e) { observers.forEach(o -> o.update(e)); }

    public Page<Flock<? extends Duck>> getAllPage(Pageable page){ return flockRepository.getAllPage(page); }
}

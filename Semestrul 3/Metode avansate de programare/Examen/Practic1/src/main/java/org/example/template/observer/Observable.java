package org.example.template.observer;

import org.example.template.observer.events.EntityChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o){
        observers.add(o);
    }

    public void removeObserver(Observer o){
        observers.remove(o);
    }

    protected void notifyObservers(EntityChangeEvent event){
        for(Observer o : observers){
            o.update(event);
        }
    }
}

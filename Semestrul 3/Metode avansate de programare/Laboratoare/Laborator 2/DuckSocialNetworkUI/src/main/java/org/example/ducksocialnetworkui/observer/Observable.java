package org.example.ducksocialnetworkui.observer;

import org.example.ducksocialnetworkui.event.UserEvent;

public interface Observable<E extends UserEvent> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E e);
}

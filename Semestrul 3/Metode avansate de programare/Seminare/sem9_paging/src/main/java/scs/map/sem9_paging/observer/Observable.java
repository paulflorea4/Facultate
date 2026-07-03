package scs.map.sem9_paging.observer;


import scs.map.sem9_paging.util.event.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E e);
}

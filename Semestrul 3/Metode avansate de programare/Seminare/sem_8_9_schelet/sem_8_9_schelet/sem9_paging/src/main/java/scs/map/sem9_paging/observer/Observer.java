package scs.map.sem9_paging.observer;


import scs.map.sem9_paging.util.event.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
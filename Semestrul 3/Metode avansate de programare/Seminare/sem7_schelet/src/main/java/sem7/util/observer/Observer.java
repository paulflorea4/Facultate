package sem7.util.observer;


import sem7.util.event.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
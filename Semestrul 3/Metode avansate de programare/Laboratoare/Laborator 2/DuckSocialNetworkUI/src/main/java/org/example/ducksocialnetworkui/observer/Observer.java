package org.example.ducksocialnetworkui.observer;

import org.example.ducksocialnetworkui.event.UserEvent;

public interface Observer<E extends UserEvent> {
    void update(E e);
}

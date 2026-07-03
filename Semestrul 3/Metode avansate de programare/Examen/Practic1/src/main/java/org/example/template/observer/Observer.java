package org.example.template.observer;

import org.example.template.observer.events.EntityChangeEvent;

public interface Observer {
    void update(EntityChangeEvent event);
}

package org.example.laboratorGUI.observer;

import org.example.laboratorGUI.utils.event.EntityChangeEvent;

public interface Observer {
    void update(EntityChangeEvent event);
}

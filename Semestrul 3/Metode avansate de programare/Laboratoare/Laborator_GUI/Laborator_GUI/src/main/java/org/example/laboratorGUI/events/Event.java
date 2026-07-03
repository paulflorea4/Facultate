package org.example.laboratorGUI.events;

import org.example.laboratorGUI.domain.Entity;
import org.example.laboratorGUI.utils.types.TipEvent;

import java.util.Objects;

public class Event extends Entity<Long> {
    private final String name;
    private final TipEvent type;
    private final String status;
    private Integer subscribers;

    public Event(Long id, String name, TipEvent type, String status) {
        super(id);
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public Event(Long id, String name, TipEvent type, String status, Integer subscribers) {
        super(id);
        this.name = name;
        this.type = type;
        this.status = status;
        this.subscribers = subscribers;
    }

    public Long getEventID() { return super.getId(); }

    public String getName() {
        return name;
    }

    public TipEvent getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public String toString() {
        return "Event id = " + super.getId() + ", name = " + name + ", type = " + type + ", subscribers = " + subscribers;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Event event)) return false;
        return Objects.equals(super.getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.getId());
    }
}

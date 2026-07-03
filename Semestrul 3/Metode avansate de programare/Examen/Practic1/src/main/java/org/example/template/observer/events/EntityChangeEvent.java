package org.example.template.observer.events;

public class EntityChangeEvent {
    private final EntityChangeEventType type;
    private final Object data;

    public EntityChangeEvent(EntityChangeEventType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public EntityChangeEventType getType() { return type; }
    public Object getData() { return data; }
}

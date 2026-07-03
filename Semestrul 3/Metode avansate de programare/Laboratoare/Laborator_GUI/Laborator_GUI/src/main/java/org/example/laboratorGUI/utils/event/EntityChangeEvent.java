package org.example.laboratorGUI.utils.event;

public class EntityChangeEvent {
    private final EntityChangeEventType type;
    private final Object data;
    private final Object oldData;

    public EntityChangeEvent(EntityChangeEventType type, Object data) {
        this.type = type;
        this.data = data;
        oldData = null;
    }

    public EntityChangeEvent(EntityChangeEventType type, Object data, Object oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public EntityChangeEventType getType() { return type; }
    public Object getData() { return data; }
    public Object getOldData() { return oldData; }
}

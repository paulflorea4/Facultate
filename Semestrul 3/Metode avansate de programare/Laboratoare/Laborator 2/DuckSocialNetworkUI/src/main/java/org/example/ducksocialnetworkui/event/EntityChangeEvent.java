package org.example.ducksocialnetworkui.event;

public class EntityChangeEvent<E> implements UserEvent {
    private EntityChangeEventType changeType;
    private E data;
    private E oldData;

    public EntityChangeEvent(EntityChangeEventType changeType, E data) {
        this.changeType = changeType;
        this.data = data;
    }

    public EntityChangeEvent(EntityChangeEventType changeType, E data, E oldData) {
        this.changeType = changeType;
        this.data = data;
        this.oldData = oldData;
    }

    public EntityChangeEventType getChangeType() {
        return changeType;
    }

    public E getData() {
        return data;
    }

    public E getOldData() {
        return oldData;
    }
}

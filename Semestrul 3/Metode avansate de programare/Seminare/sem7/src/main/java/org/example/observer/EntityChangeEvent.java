package org.example.observer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class EntityChangeEvent<E> implements Event {
    public enum ChangeType {
        ADD,
        UPDATE,
        DELETE
    }

    private ChangeType type;
    private E entity;
}

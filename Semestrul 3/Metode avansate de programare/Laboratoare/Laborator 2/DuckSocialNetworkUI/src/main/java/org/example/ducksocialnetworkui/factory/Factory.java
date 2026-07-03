package org.example.ducksocialnetworkui.factory;

public interface Factory<T> {
    T create(Object... params);
}

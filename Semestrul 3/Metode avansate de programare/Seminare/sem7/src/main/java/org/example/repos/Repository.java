package org.example.repos;


import org.example.models.Entity;

import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {
    Optional<E> findById(ID id);

    Iterable<E> getAll();

    Optional<E> save(E e);

    Optional<E> update(E e);

    Optional<E> delete(ID id);
}

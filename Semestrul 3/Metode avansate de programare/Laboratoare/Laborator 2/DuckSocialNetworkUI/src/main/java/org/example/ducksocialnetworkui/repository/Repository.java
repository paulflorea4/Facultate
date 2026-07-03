package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.Entity;

import java.util.Optional;

public interface Repository<ID,E extends Entity<ID>> {
    Optional<E> findOne(ID id);

    Iterable<E> findAll();

    Optional<E> save(E entity);

    Optional<E> delete(ID id);
}

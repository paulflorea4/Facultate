package org.example.template.repository;

import org.example.template.domain.Entity;

public interface Repository<ID, TElem extends Entity<ID>> {
    void save(TElem entity);
    void delete(ID id);
    void update(ID id, TElem entity);
    TElem findById(ID id);
    Iterable<TElem> findAll();
}

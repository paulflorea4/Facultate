package org.example.laboratorGUI.repository;

import org.example.laboratorGUI.domain.Entity;

public interface Repository<ID, TElem extends Entity<ID>> {
    void save(TElem entity);
    TElem findById(ID id);
    Iterable<TElem> findAll();
    void delete(ID id);
}

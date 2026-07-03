package org.example.laboratorGUI.repository;

import org.example.laboratorGUI.domain.Entity;

public interface PagingRepository<ID, TElem extends Entity<ID>> extends Repository<ID, TElem>{
    Iterable<TElem> findPage(int pageNumber, int pageSize);
}

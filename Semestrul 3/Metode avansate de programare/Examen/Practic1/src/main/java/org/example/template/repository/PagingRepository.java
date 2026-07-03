package org.example.template.repository;

import org.example.template.domain.Entity;

public interface PagingRepository<ID, TElem extends Entity<ID>> extends Repository<ID, TElem> {
    Iterable<TElem> findPage(int pageNumber, int pageSize);
}

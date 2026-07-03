package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.Entity;
import org.example.ducksocialnetworkui.dto.Dto;
import org.example.ducksocialnetworkui.utils.paging.Page;
import org.example.ducksocialnetworkui.utils.paging.Pageable;

public interface PagedRepository<ID,E extends Entity<ID>> extends Repository<ID,E> {
    Page<E> getAllPage(Pageable page);

    Page<E> getAllPage(Pageable page, Dto filter);
}

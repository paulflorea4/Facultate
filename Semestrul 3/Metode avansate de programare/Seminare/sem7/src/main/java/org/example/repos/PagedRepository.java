package org.example.repos;

import org.example.models.Entity;
import org.example.models.User;
import org.example.repos.dtos.Page;
import org.example.repos.dtos.Pageable;

public interface PagedRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> getAllPage(Pageable page);
}

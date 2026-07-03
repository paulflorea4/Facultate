package org.example.repos;

import org.example.models.Entity;
import org.example.models.User;
import org.example.repos.dtos.Page;
import org.example.repos.dtos.Pageable;

public interface UserRepository extends PagedRepository<Integer, User> {
    //Page<User> getAllPage(Pageable page, UserFilterDto filter);
}

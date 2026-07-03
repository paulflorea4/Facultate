package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.User;
import org.example.ducksocialnetworkui.utils.paging.Page;
import org.example.ducksocialnetworkui.utils.paging.Pageable;

import java.util.Optional;

public interface UserRepository extends PagedRepository<Long, User>{
    Optional<User> findByUsername(String username);

    Page<User> findFriends(Pageable page, Long id);
}

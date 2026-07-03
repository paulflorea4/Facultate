package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.FriendRequest;
import org.example.ducksocialnetworkui.domain.FriendRequestStatus;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends Repository<Long, FriendRequest>{
    Optional<FriendRequest> findByUsers(Long from, Long to);
    List<FriendRequest> findPendingForUser(Long userId);
    Optional<FriendRequest> updateStatus(Long id, FriendRequestStatus status);
    List<FriendRequest> findAllForUser(Long id);
}

package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.Friendship;
import org.example.ducksocialnetworkui.domain.FriendshipId;

public interface FriendshipRepository extends PagedRepository<FriendshipId, Friendship> {
}

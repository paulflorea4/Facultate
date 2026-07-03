package org.example.ducksocialnetworkui.domain;

import java.util.Objects;

public class Friendship extends Entity<FriendshipId> {

    private final Long userId1;
    private final Long userId2;

    public Friendship(Long a, Long b) {
        if (Objects.equals(a, b))
            throw new IllegalArgumentException("Un user nu poate fi prieten cu el insusi.");

        this.userId1 = Math.min(a, b);
        this.userId2 = Math.max(a, b);

        this.id = new FriendshipId(this.userId1, this.userId2);
    }

    public Long getUserId1() { return userId1; }
    public Long getUserId2() { return userId2; }

    @Override
    public String toString() {
        return "Friendship [userId1=" + userId1 + ", userId2=" + userId2 + "]";
    }
}

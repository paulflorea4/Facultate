package org.example.ducksocialnetworkui.domain;

import java.util.Objects;

public class FriendshipId {
    private final Long userId1;
    private final Long userId2;

    public FriendshipId(Long userId1, Long userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }

    public Long getUserId1() { return userId1; }
    public Long getUserId2() { return userId2; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendshipId that)) return false;
        return Objects.equals(userId1, that.userId1)
                && Objects.equals(userId2, that.userId2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId1, userId2);
    }
}
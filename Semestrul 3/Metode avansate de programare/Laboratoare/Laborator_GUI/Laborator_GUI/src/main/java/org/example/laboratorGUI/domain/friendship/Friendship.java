package org.example.laboratorGUI.domain.friendship;

import org.example.laboratorGUI.domain.Entity;
import org.example.laboratorGUI.utils.types.TipFriendship;

import java.util.Objects;

public class Friendship extends Entity<Long> {
    private final long userId1;
    private final long userId2;
    private TipFriendship status;
    private final long sentTo;

    public Friendship(Long id, Long userId1, Long userId2, TipFriendship status) {
        super(id);
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.status = status;
        sentTo = -1;
    }

    public Friendship(Long id, Long userId1, Long userId2, TipFriendship status, Long sentTo) {
        super(id);
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.status = status;
        this.sentTo = sentTo;
    }

    public Long getFSID() {
        return super.getId();
    }

    public long getUserId1() {
        return userId1;
    }

    public long getUserId2() {
        return userId2;
    }

    public TipFriendship getStatus() {
        return status;
    }

    public long getSender() {
        return sentTo == userId1 ? userId2 : userId1;
    }

    public long getSentTo() {
        return sentTo;
    }

    public void acceptRequest() {
        status = TipFriendship.ACCEPTED;
    }

    public void rejectRequest() { status = TipFriendship.REJECTED; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return (userId1 == that.userId1 && userId2 == that.userId2) || (userId1 == that.userId2 && userId2 == that.userId1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.min(userId1, userId2), Math.max(userId1, userId2));
    }
}

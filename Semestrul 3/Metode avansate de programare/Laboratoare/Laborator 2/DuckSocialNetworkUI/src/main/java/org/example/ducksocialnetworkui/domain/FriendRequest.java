package org.example.ducksocialnetworkui.domain;

import java.time.LocalDateTime;

public class FriendRequest extends Entity<Long> {
    private Long fromId;
    private Long toId;
    private FriendRequestStatus status;
    private LocalDateTime date;

    public FriendRequest(Long aLong, Long fromId, Long toId, FriendRequestStatus status, LocalDateTime date) {
        super(aLong);
        this.fromId = fromId;
        this.toId = toId;
        this.status = status;
        this.date = date;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return date +": Friend request from "+fromId+ ".Status: "+status;
    }
}

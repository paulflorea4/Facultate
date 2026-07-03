package org.example.ducksocialnetworkui.dto;

import java.io.Serializable;

public class FriendshipDto implements Dto {
    private Long friendId;

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
}

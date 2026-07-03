package org.example.ducksocialnetworkui.repository;

import org.example.ducksocialnetworkui.domain.Message;

import java.util.List;

public interface MessageRepository extends Repository<Long, Message> {
    List<Message> getMessagesBetweenUsers(Long user1, Long user2);
}

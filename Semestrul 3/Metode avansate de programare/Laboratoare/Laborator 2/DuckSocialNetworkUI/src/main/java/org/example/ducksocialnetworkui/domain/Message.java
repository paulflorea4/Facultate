package org.example.ducksocialnetworkui.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Long> {
    private Long from;
    private List<Long> to;
    private String message;
    private LocalDateTime data;
    private Message reply;

    public Message(Long id, Long from, String message, LocalDateTime data) {
        super(id);
        this.from = from;
        this.to = new ArrayList<>();
        this.message = message;
        this.data = data;
        this.reply = null;
    }

    public Long getFrom() {
        return from;
    }

    public List<Long> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Message getReply() {
        return reply;
    }

    public void addTo(Long userId) { to.add(userId); }

    public void setReply(Message reply) { this.reply = reply; }

    @Override
    public String toString() {
        return message+ " ' " + data;
    }
}

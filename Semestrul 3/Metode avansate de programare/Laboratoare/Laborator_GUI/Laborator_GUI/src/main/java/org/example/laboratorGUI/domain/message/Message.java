package org.example.laboratorGUI.domain.message;

import org.example.laboratorGUI.domain.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Long> {
    private final long from;
    private final List<Long> to;
    private final String message;
    private final LocalDateTime date;

    private Message reply;

    public Message(Long id, Long from, String message, LocalDateTime date) {
        super(id);
        this.from = from;
        this.message = message;
        this.date = date;
        to =  new ArrayList<>();
    }

    public Long getMessageID() { return super.getId(); }

    public Long getFrom() {
        return from;
    }

    public List<Long> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Message getReply() {
        return reply;
    }

    public void addTo(long user) {
        to.add(user);
    }

    public void replyTo(Message other) {
        this.reply = other;
    }
}

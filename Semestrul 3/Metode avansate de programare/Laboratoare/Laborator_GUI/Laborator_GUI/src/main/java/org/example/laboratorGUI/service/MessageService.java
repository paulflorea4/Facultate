package org.example.laboratorGUI.service;

import org.example.laboratorGUI.domain.message.Message;
import org.example.laboratorGUI.observer.Observable;
import org.example.laboratorGUI.repository.database.DBMessageRepository;
import org.example.laboratorGUI.utils.event.EntityChangeEvent;
import org.example.laboratorGUI.utils.event.EntityChangeEventType;
import org.example.laboratorGUI.validators.MessageValidator;
import org.example.laboratorGUI.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class MessageService extends Observable {
    private final DBMessageRepository msgRepo;

    private final Validator<Message> validator = new MessageValidator();

    public MessageService(DBMessageRepository msgRepo) {
        this.msgRepo = msgRepo;
    }

    public void sendMessage(Long from, List<Long> to, String text) {
        Message msg = new Message(1L, from, text, LocalDateTime.now());
        to.forEach(msg::addTo);
        validator.validate(msg);
        msgRepo.save(msg);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.MESSAGE_RECEIVED, msg));
    }

    public void replyToMessage(Message original, Long from, List<Long> to, String text) {
        Message msg = new Message(1L, from, text, LocalDateTime.now());
        to.forEach(msg::addTo);
        msg.replyTo(original);
        validator.validate(msg);
        msgRepo.save(msg);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.MESSAGE_RECEIVED, msg));
    }

    public List<Message> getConversation(Long a, Long b) {
        return msgRepo.findConversation(a, b);
    }
}


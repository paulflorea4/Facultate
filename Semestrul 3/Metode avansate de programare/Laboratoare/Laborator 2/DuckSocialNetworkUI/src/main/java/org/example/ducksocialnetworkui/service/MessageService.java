package org.example.ducksocialnetworkui.service;

import org.example.ducksocialnetworkui.domain.Message;
import org.example.ducksocialnetworkui.domain.User;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.EntityChangeEventType;
import org.example.ducksocialnetworkui.observer.Observable;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.repository.MessageRepository;
import org.example.ducksocialnetworkui.validation.Validator;
import org.example.ducksocialnetworkui.validation.ValidatorMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageService implements Observable<EntityChangeEvent<Message>> {
    private final MessageRepository messageRepository;
    private final Validator<Message> validator = new ValidatorMessage();
    private final List<Observer<EntityChangeEvent<Message>>> observers=new ArrayList<>();

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void sendMessage(Long from, List<Long> to, String text) {
        Message message=new Message(null,from,text, LocalDateTime.now());
        to.forEach(message::addTo);
        validator.validate(message);
        Optional<Message> savedMessage = messageRepository.save(message);

        savedMessage.ifPresent(msg ->
                notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.MESSAGE_RECEIVED, msg))
        );
    }

    public void replyMessage(Message original,Long from,List<Long> to, String text) {
        Message message=new Message(null,from,text, LocalDateTime.now());
        to.forEach(message::addTo);
        message.setReply(original);
        validator.validate(message);
        Optional<Message> savedMessage = messageRepository.save(message);
        savedMessage.ifPresent(msg ->
                notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.MESSAGE_RECEIVED, msg))
        );
    }

    public void typingStarted(Long from, Long to) {
        notifyObservers(new EntityChangeEvent<>(
                EntityChangeEventType.TYPING_STARTED,
                new Message(null, from, "", LocalDateTime.now()) {{
                    addTo(to);
                }}
        ));
    }

    public void typingStopped(Long from, Long to) {
        notifyObservers(new EntityChangeEvent<>(
                EntityChangeEventType.TYPING_STOPPED,
                new Message(null, from, "", LocalDateTime.now()) {{
                    addTo(to);
                }}
        ));
    }

    public List<Message> getMessagesBetweenUsers(Long user1, Long user2) {
        return messageRepository.getMessagesBetweenUsers(user1, user2);
    }

    @Override
    public void addObserver(Observer<EntityChangeEvent<Message>> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent<Message>> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(EntityChangeEvent<Message> e) {
        observers.forEach(o -> o.update(e));
    }
}

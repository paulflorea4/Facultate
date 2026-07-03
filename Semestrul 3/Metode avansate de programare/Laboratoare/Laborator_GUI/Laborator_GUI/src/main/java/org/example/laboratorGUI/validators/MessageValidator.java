package org.example.laboratorGUI.validators;

import org.example.laboratorGUI.domain.message.Message;
import org.example.laboratorGUI.exceptions.ValidationException;

public class MessageValidator implements Validator<Message> {
    public void validate(Message m) throws ValidationException {
        if (m == null)
            throw new ValidationException("Null object!");
        if (m.getTo() == null || m.getTo().isEmpty())
            throw new ValidationException("Recipient(s) required!");
        if (m.getMessage() == null || m.getMessage().isBlank())
            throw new ValidationException("Message required!");
    }
}

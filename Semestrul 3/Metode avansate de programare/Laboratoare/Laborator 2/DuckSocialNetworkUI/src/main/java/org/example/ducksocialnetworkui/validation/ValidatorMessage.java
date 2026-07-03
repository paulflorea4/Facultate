package org.example.ducksocialnetworkui.validation;

import org.example.ducksocialnetworkui.domain.Message;
import org.example.ducksocialnetworkui.exception.ValidationException;

public class ValidatorMessage implements Validator<Message>{
    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity == null)
            throw new ValidationException("Mesajul e null!");
        if(entity.getTo()==null ||  entity.getTo().isEmpty())
            throw new ValidationException("Mesajul trebuie trimis cuiva!");
        if(entity.getMessage()==null || entity.getMessage().isBlank())
            throw new ValidationException("Mesaj invalid!");
    }
}

package org.example.ducksocialnetworkui.validation;

import org.example.ducksocialnetworkui.event.Event;
import org.example.ducksocialnetworkui.exception.ValidationException;

public class ValidatorEvent implements Validator<Event>{
    @Override
    public void validate(Event event) throws ValidationException {
        StringBuilder errors = new StringBuilder();

        if(event.getName()==null || event.getName().isBlank())
            errors.append("Nume invalid.");

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }
}

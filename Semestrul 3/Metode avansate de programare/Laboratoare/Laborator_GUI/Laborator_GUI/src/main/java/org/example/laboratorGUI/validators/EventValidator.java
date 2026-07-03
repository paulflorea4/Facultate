package org.example.laboratorGUI.validators;

import org.example.laboratorGUI.events.Event;
import org.example.laboratorGUI.exceptions.ValidationException;

public class EventValidator implements Validator<Event>{
    public void validate(Event e) throws ValidationException {
        if (e == null)
            throw new ValidationException("Null object!");
        if (e.getName() == null || e.getName().isBlank())
            throw new ValidationException("Name required!");
        if (e.getType() == null)
            throw new ValidationException("Invalid event type!");
    }
}

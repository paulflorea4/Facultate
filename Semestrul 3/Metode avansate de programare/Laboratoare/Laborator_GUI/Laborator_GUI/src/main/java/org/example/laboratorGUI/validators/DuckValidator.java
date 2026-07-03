package org.example.laboratorGUI.validators;

import org.example.laboratorGUI.domain.user.duck.Duck;
import org.example.laboratorGUI.exceptions.ValidationException;

public class DuckValidator implements Validator<Duck>{
    public void validate(Duck d) throws ValidationException {
        if (d == null)
            throw new ValidationException("Null object!");
        if (d.getUsername() == null || d.getUsername().isBlank())
            throw new ValidationException("Username required!");
        if (d.getSpeed() <= 0)
            throw new ValidationException("Invalid speed value!");
        if (d.getResistance() <= 0)
            throw new ValidationException("Invalid resistance value!");
        if (d.getType() == null)
            throw new ValidationException("Invalid duck type!");
    }
}

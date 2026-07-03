package org.example.laboratorGUI.validators;

import org.example.laboratorGUI.domain.flock.Flock;
import org.example.laboratorGUI.domain.user.duck.Duck;
import org.example.laboratorGUI.exceptions.ValidationException;

public class FlockValidator implements Validator<Flock<? extends Duck>> {
    public void validate(Flock<? extends Duck> flock)
    {
        if (flock == null)
            throw new ValidationException("Null object!");
        if (flock.getName() == null || flock.getName().isBlank())
            throw new ValidationException("Name required!");
    }
}

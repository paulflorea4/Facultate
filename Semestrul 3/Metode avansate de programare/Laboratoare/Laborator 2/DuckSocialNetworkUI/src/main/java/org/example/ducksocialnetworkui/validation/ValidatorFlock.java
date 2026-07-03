package org.example.ducksocialnetworkui.validation;

import org.example.ducksocialnetworkui.domain.Duck;
import org.example.ducksocialnetworkui.exception.ValidationException;
import org.example.ducksocialnetworkui.flock.Flock;

public class ValidatorFlock implements Validator<Flock<? extends Duck>>{
    @Override
    public void validate(Flock<? extends Duck> flock) {
        StringBuilder errors = new StringBuilder();
        if(flock.getName()==null || flock.getName().isBlank())
            errors.append("Nume invalid.");

        if (!errors.isEmpty())
            throw new ValidationException(errors.toString());
    }
}

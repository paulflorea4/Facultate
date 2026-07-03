package org.example.ducksocialnetworkui.validation;

import org.example.ducksocialnetworkui.domain.Duck;
import org.example.ducksocialnetworkui.exception.ValidationException;

public class ValidatorDuck implements Validator<Duck> {
    @Override
    public void validate(Duck d) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        if (d.getUsername() == null || d.getUsername().isBlank())
            errors.append("Username invalid.\n");
        if (d.getEmail() == null || !d.getEmail().contains("@"))
            errors.append("Email invalid.\n");
        if(d.getPassword() == null || d.getPassword().isBlank())
            errors.append("Parola invalid.\n");
        if (d.getViteza() <= 0)
            errors.append("Viteza trebuie sa fie  pozitiva.\n");
        if (d.getRezistenta() <= 0)
            errors.append("Rezistenta trebuie sa fie  pozitiva.");
        if(d.getCardId()<=0)
            errors.append("Card Id invalid.\n");

        if (!errors.isEmpty())
            throw new ValidationException(errors.toString());
    }
}
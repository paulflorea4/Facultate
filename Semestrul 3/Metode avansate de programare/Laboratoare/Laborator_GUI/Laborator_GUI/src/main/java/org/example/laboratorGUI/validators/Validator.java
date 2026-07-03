package org.example.laboratorGUI.validators;

import org.example.laboratorGUI.exceptions.ValidationException;

public interface Validator<TElem> {
    void validate(TElem entity) throws ValidationException;
}

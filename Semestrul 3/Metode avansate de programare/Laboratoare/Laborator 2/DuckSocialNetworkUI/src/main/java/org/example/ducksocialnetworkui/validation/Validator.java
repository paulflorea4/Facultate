package org.example.ducksocialnetworkui.validation;

import org.example.ducksocialnetworkui.exception.ValidationException;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}

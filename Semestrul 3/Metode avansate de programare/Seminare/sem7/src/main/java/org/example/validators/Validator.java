package org.example.validators;

public interface Validator<T> {
    void validate(T item) throws ValidationException;
}

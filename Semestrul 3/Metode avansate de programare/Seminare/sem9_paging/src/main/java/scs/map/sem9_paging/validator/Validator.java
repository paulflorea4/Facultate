package scs.map.sem9_paging.validator;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}

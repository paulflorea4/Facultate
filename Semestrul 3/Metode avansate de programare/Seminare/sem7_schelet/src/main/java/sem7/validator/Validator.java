package sem7.validator;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}

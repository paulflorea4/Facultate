package socialnetwork.domain.model.validators;

import socialnetwork.domain.model.Student;

public class StudentValidator implements Validator<Student> {

    @Override
    public void validate(Student entity) throws ValidationException {
        if (entity == null) {
            throw new ValidationException("Entity cannot be null");
        }
        if (entity.getId() == null) {
            throw new ValidationException("Id cannot be null");
        }
        if (entity.getName() == null || entity.getName().isBlank())
            throw new ValidationException("Name cannot be null or blank");
        if (entity.getGrade() < 0 || entity.getGrade() > 10)
            throw new ValidationException("Grade must be between 0 and 10");
    }
}

package org.example.validators;

import org.example.models.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User item) throws ValidationException {
        StringBuilder builder = new StringBuilder();

        if (item.getCreateDate() == null) {
            builder.append("Create Date is required; ");
        }

        if (item.getCredits() < 0) {
            builder.append("Credits must be positive; ");
        }

        if (!builder.isEmpty())
            throw new ValidationException(builder.toString());
    }
}

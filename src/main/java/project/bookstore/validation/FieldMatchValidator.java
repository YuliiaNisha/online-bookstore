package project.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import project.bookstore.dto.user.UserRegistrationRequestDto;
import project.bookstore.exception.FieldMatchValidationException;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {
    private String first;
    private String second;
    private String message;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(
            UserRegistrationRequestDto requestDto,
            ConstraintValidatorContext context
    ) {
        try {
            Field firstField = requestDto.getClass().getDeclaredField(first);
            Field secondField = requestDto.getClass().getDeclaredField(second);
            firstField.setAccessible(true);
            secondField.setAccessible(true);
            if (!firstField.getType().equals(secondField.getType())) {
                throw new FieldMatchValidationException(
                        "Field types do not match. First: "
                        + firstField.getType().getName()
                                + " second: " + secondField.getType().getName());
            }
            Object firstValue = firstField.get(requestDto);
            Object secondValue = secondField.get(requestDto);
            if (firstValue == null
                    || secondValue == null
                        || !firstValue.equals(secondValue)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(second)
                        .addConstraintViolation();
                return false;
            }
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FieldMatchValidationException("Can't validate fields: ", e);
        }
    }
}

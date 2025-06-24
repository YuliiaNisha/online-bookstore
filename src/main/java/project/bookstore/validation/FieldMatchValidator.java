package project.bookstore.validation;

import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import project.bookstore.exception.FieldMatchValidationException;

public class FieldMatchValidator extends AbstractFieldValidator<FieldMatch> {
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
            Object object,
            ConstraintValidatorContext context
    ) {
        try {
            Field firstField = getField(object, first);
            Field secondField = getField(object, second);
            isSameType(firstField, secondField);
            Object firstValue = firstField.get(object);
            Object secondValue = secondField.get(object);
            if (!compare(firstValue, secondValue)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(second)
                        .addConstraintViolation();
                return false;
            }
            return true;
        } catch (IllegalAccessException e) {
            throw new FieldMatchValidationException("Can't validate fields: ", e);
        }
    }

    @Override
    protected boolean compare(Object firstValue, Object secondValue) {
        return firstValue != null && firstValue.equals(secondValue);
    }
}

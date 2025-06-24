package project.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import project.bookstore.exception.FieldMatchValidationException;

public abstract class AbstractFieldValidator<A extends Annotation>
        implements ConstraintValidator<A, Object> {
    protected Field getField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new FieldMatchValidationException("Can't validate fields: ", e);
        }
    }

    protected void isSameType(Field firstField, Field secondField) {
        if (!firstField.getType().equals(secondField.getType())) {
            throw new FieldMatchValidationException(
                    "Field types do not match. First: "
                            + firstField.getType().getName()
                            + " second: " + secondField.getType().getName());
        }
    }

    protected abstract boolean compare(Object firstValue, Object secondValue);
}

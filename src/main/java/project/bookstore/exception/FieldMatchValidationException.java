package project.bookstore.exception;

public class FieldMatchValidationException extends RuntimeException {
    public FieldMatchValidationException(String message) {
        super(message);
    }

    public FieldMatchValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

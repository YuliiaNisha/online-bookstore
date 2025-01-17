package project.bookstore.exception;

public class SpecificationProviderNotFoundException extends RuntimeException {
    public SpecificationProviderNotFoundException(String message) {
        super(message);
    }
}

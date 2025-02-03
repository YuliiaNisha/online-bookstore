package project.bookstore.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
                            MethodArgumentNotValidException ex,
                                     HttpHeaders headers,
                                     HttpStatusCode status,
                                     WebRequest request
    ) {
        List<String> errors = ex.getBindingResult()
                                .getAllErrors()
                                .stream()
                                .map(this::getErrorMessage)
                                .toList();
        return new ResponseEntity<>(
                    getBody(HttpStatus.BAD_REQUEST,errors),
                    headers,
                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(
                            EntityNotFoundException ex
    ) {
        return new ResponseEntity<>(
                    getBody(HttpStatus.NOT_FOUND, List.of(ex.getMessage())),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SpecificationProviderNotFoundException.class)
    protected ResponseEntity<Object> handleSpecificationProviderNotFoundException(
                            SpecificationProviderNotFoundException ex
    ) {
        return new ResponseEntity<>(
                    getBody(HttpStatus.INTERNAL_SERVER_ERROR, List.of(ex.getMessage())),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            return fieldError.getField() + ": " + error.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }

    private Map<String, Object> getBody(HttpStatus status, List<String> message
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("errors", message);
        return body;
    }
}

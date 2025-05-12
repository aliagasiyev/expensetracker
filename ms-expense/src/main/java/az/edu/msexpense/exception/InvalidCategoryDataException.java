package az.edu.msexpense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCategoryDataException extends RuntimeException {
    public InvalidCategoryDataException(String message) {
        super(message);
    }
} 
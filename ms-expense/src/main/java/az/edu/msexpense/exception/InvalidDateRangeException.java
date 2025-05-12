package az.edu.msexpense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String message) {
        super(message);
    }

    public InvalidDateRangeException() {
        super("Invalid date range: 'from' date must be before or equal to 'to' date");
    }
} 
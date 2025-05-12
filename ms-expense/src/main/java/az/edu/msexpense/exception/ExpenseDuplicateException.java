package az.edu.msexpense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ExpenseDuplicateException extends RuntimeException {
    public ExpenseDuplicateException(String message) {
        super(message);
    }
}

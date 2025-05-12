package az.edu.msexpense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExpenseDateException extends RuntimeException {
    public ExpenseDateException(String message) {
        super(message);
    }
}

package az.edu.msexpense.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ExpensePermissionException extends RuntimeException {
    public ExpensePermissionException(String message) {
        super(message);
    }
}

package az.edu.msanalytics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse("Bu əməliyyata icazəniz yoxdur", "ACCESS_DENIED", 403, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return buildErrorResponse("Authentication tələb olunur", "UNAUTHORIZED", 401, request);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<?> handleCredentialsNotFound(AuthenticationCredentialsNotFoundException ex, WebRequest request) {
        return buildErrorResponse("Authentication tələb olunur", "UNAUTHORIZED", 401, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), "INTERNAL_ERROR", 500, request);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, String code, int status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("code", code);
        body.put("status", status);
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.valueOf(status));
    }
} 
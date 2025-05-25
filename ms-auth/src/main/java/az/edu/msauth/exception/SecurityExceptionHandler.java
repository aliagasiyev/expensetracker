package az.edu.msauth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class SecurityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Bu əməliyyata icazəniz yoxdur");
        error.put("code", "ACCESS_DENIED");
        error.put("status", HttpStatus.FORBIDDEN.value());
        error.put("timestamp", LocalDateTime.now());
        error.put("path", getCurrentPath());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthentication(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Autentifikasiya uğursuz oldu");
        error.put("code", "AUTHENTICATION_FAILED");
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        error.put("timestamp", LocalDateTime.now());
        error.put("path", getCurrentPath());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCredentialsNotFound(AuthenticationCredentialsNotFoundException ex) {
        log.warn("Authentication credentials not found: {}", ex.getMessage());
        
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Autentifikasiya məlumatları tapılmadı");
        error.put("code", "CREDENTIALS_NOT_FOUND");
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        error.put("timestamp", LocalDateTime.now());
        error.put("path", getCurrentPath());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    private String getCurrentPath() {
        // Bu method-u request context-dən path əldə etmək üçün istifadə edə bilərsiniz
        return "/api/path"; // Placeholder
    }
} 
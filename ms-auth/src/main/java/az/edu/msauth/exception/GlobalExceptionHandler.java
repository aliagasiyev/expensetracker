package az.edu.msauth.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found exception: {}", ex.getMessage());
        return new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error("User already exists exception: {}", ex.getMessage());
        return new ApiError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Bad credentials exception: {}", ex.getMessage());
        return new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid email or password",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied exception: {}", ex.getMessage());
        return new ApiError(
                HttpStatus.FORBIDDEN.value(),
                "Access denied",
                LocalDateTime.now()
        );
    }

    // JWT Exceptions
    @ExceptionHandler({ExpiredJwtException.class, MalformedJwtException.class, SignatureException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleJwtExceptions(Exception ex) {
        log.error("JWT exception: {}", ex.getMessage());
        return new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid or expired token",
                LocalDateTime.now()
        );
    }

    // Validation Exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.error("Validation error: {}", errors);
        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                LocalDateTime.now(),
                errors
        );
    }



    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        log.error("Constraint violation: {}", errors);
        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                LocalDateTime.now(),
                errors
        );
    }

    // Request Parameter Exceptions
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParams(MissingServletRequestParameterException ex) {
        log.error("Missing parameter: {}", ex.getMessage());
        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Missing required parameter: " + ex.getParameterName(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Type mismatch: {}", ex.getMessage());
        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid parameter type for: " + ex.getName(),
                LocalDateTime.now()
        );
    }

    // File Upload Exceptions
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMaxSizeException(MaxUploadSizeExceededException ex) {
        log.error("File size exceeded: {}", ex.getMessage());
        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "File too large",
                LocalDateTime.now()
        );
    }

    // Fallback Handler
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAllUncaughtException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                LocalDateTime.now()
        );
    }
}
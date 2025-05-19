package az.edu.msauth.exception;

public class ApiError extends RuntimeException {
    public ApiError(String message) {
        super(message);
    }
}

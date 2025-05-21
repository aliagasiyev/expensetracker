package az.edu.msexpense.security;

import az.edu.msexpense.client.TokenValidationResponse;

public interface SecurityUtils {
    TokenValidationResponse getCurrentUser();

    boolean isAdmin();

    Long getCurrentUserId();

    void validateUserAccess(Long resourceUserId);
}
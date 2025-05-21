package az.edu.msexpense.security;

import az.edu.msexpense.client.TokenValidationResponse;
import az.edu.msexpense.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityUtilsImpl implements SecurityUtils {

    @Override
    public TokenValidationResponse getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof TokenValidationResponse) {
            return (TokenValidationResponse) auth.getPrincipal();
        }
        throw new UnauthorizedException("User not authenticated");
    }

    @Override
    public boolean isAdmin() {
        return getCurrentUser().getRole().equals("ADMIN");
    }

    @Override
    public Long getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    @Override
    public void validateUserAccess(Long resourceUserId) {
        TokenValidationResponse currentUser = getCurrentUser();
        if (!isAdmin() && !currentUser.getUserId().equals(resourceUserId)) {
            throw new UnauthorizedException("Not authorized to access this resource");
        }
    }
}
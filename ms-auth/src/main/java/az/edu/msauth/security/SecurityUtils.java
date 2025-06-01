package az.edu.msauth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SecurityUtils {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Getting current user ID, authentication: {}", authentication);
        log.debug("Authentication class: {}", authentication != null ? authentication.getClass() : "null");
        log.debug("Is authenticated: {}", authentication != null ? authentication.isAuthenticated() : "null");
        
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication instanceof JwtAuthenticationToken) {
                JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
                Long userId = jwtAuth.getUserId();
                log.debug("Extracted userId from JwtAuthenticationToken: {}", userId);
                return userId;
            } else {
                log.debug("Authentication is not JwtAuthenticationToken, it's: {}", authentication.getClass());
                log.debug("Authentication details: {}", authentication);
            }
        } else {
            log.debug("Authentication is null or not authenticated");
        }
        return null;
    }
}
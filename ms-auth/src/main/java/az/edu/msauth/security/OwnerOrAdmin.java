package az.edu.msauth.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for endpoints that can be accessed by:
 * - ADMIN role (can access any resource)
 * - USER role (can only access their own resources)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
public @interface OwnerOrAdmin {
} 
package az.edu.msauth.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            log.debug("Processing JWT token: {}", jwt.substring(0, Math.min(jwt.length(), 20)) + "...");
            final Claims claims = jwtService.extractAllClaims(jwt);
            final String userEmail = claims.getSubject();
            log.debug("Extracted email from token: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                log.debug("Loaded user details for: {}, authorities: {}", userEmail, userDetails.getAuthorities());

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Create a custom authentication token that stores the user ID directly
                    JwtAuthenticationToken authToken = new JwtAuthenticationToken(
                            userDetails,
                            userDetails.getAuthorities(),
                            claims.get("userId", Long.class)
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Successfully set authentication for user: {}", userEmail);
                    log.debug("Authentication object: {}", authToken);
                    log.debug("Authorities: {}", authToken.getAuthorities());
                } else {
                    log.debug("Token validation failed for user: {}", userEmail);
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e);
        }

        // Log final authentication state before passing to next filter
        var finalAuth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Final authentication before next filter: {}", finalAuth);
        if (finalAuth != null) {
            log.debug("Final authorities: {}", finalAuth.getAuthorities());
        }

        filterChain.doFilter(request, response);
    }
}
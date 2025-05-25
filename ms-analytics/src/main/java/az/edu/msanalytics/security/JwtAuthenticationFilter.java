package az.edu.msanalytics.security;

import az.edu.msanalytics.client.TokenValidationResponse;
import az.edu.msanalytics.client.AuthClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthClient authClient;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            TokenValidationResponse validationResponse = authClient.validateToken(jwt);

            if (validationResponse != null && validationResponse.isValid()) {
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + validationResponse.getRole())
                );

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        validationResponse,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("User {} authenticated with role {}", 
                         validationResponse.getEmail(), validationResponse.getRole());
            }
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
} 
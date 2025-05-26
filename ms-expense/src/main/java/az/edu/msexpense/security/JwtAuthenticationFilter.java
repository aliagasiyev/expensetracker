package az.edu.msexpense.security;

import az.edu.msexpense.client.AuthServiceClient;
import az.edu.msexpense.client.TokenValidationResponse;
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
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> PUBLIC_PATHS = Arrays.asList(

            // qeyd et burda olanlar log yazmir // buna diqqet et
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return PUBLIC_PATHS.stream().anyMatch(p -> pathMatcher.match(p, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.info("EXPENSE SERVISDEN GONDERILEN TOKEN: {}", authHeader.replace("Bearer ", ""));

            try {
                TokenValidationResponse validationResponse = authServiceClient.validateToken(authHeader.replace("Bearer ", ""));

                // BURADA LOG ƏLAVƏ ET!
                log.info("ValidationResponse.getRole(): '{}'", validationResponse.getRole());
                log.info("Authorities: {}", List.of(new SimpleGrantedAuthority("ROLE_" + validationResponse.getRole())));

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        validationResponse,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + validationResponse.getRole()))
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception e) {
                log.error("Could not validate token: ", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
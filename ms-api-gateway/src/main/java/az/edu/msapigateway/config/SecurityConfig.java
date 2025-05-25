package az.edu.msapigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges
                        // 🌐 Public endpoints - hər kəs çıxış edə bilər
                        .pathMatchers("/auth/register", "/auth/login", "/auth/forgot-password").permitAll()
                        .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        
                        // 👑 Admin only routes
                        .pathMatchers("/admin/**").hasRole("ADMIN")
                        .pathMatchers("/users/all", "/users/{id}").hasRole("ADMIN")
                        .pathMatchers("/expenses/all", "/expenses/admin/**").hasRole("ADMIN")
                        .pathMatchers("/categories/admin/**").hasRole("ADMIN")
                        .pathMatchers("/analytics/system-overview", "/analytics/all-users-summary").hasRole("ADMIN")
                        
                        // 👤 User routes - authenticated users
                        .pathMatchers("/users/**", "/expenses/**", "/categories/**", "/analytics/**").hasAnyRole("USER", "ADMIN")
                        
                        // 🔒 All other requests need authentication
                        .anyExchange().authenticated()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 
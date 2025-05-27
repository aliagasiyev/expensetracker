//package az.edu.msexpense.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class WebConfig {
//
//    @Bean(name = "api1SecurityFilterChain")
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated()
//                )
//                .headers(headers -> headers
//                        .addHeaderWriter((request, response) -> {
//                            response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
//                        })
//                );
//        return http.build();
//    }
//}
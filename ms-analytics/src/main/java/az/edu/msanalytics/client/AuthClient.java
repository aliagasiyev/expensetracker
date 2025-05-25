package az.edu.msanalytics.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthClient {

    private final RestTemplate restTemplate;

    public TokenValidationResponse validateToken(String token) {
        try {
            String url = "http://ms-auth/api/auth/validate";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<TokenValidationResponse> response = restTemplate.exchange(
                    url, 
                    HttpMethod.GET, 
                    entity, 
                    TokenValidationResponse.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to validate token: {}", e.getMessage());
            return new TokenValidationResponse(false, null, null, null);
        }
    }
} 
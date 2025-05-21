package az.edu.msexpense.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-auth")
public interface AuthServiceClient {

    @GetMapping("/api/auth/validate-token")
    TokenValidationResponse validateToken(@RequestHeader("Authorization") String token);
}
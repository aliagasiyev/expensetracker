package az.edu.msauth.controller;

import az.edu.msauth.dto.request.LoginRequest;
import az.edu.msauth.dto.request.RegisterRequest;
import az.edu.msauth.dto.request.ForgotPasswordRequest;
import az.edu.msauth.dto.request.ResetPasswordRequest;
import az.edu.msauth.dto.response.AuthResponse;
import az.edu.msauth.dto.response.TokenValidationResponse;
import az.edu.msauth.dto.response.TokenValidationResponse;
import az.edu.msauth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @Operation(summary = "Initiate password reset")
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.initiatePasswordReset(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reset password with token")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Validate JWT token")
    @GetMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.validateToken(token));
    }

}
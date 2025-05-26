package az.edu.msauth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data

public class RegisterRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and contain at least one digit, one uppercase, one lowercase letter, and one special character")
    private String password;

    @Pattern(
            regexp = "^\\+?[1-9]\\d{1,14}$",
            message = "Phone number should be valid (E.164 format, e.g., +994501234567)"
    )
    private String phoneNumber;

    @NotBlank(message = "Role is required")
    private String role;
    }
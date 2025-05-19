package az.edu.msauth.service;

import az.edu.msauth.dto.request.*;
import az.edu.msauth.dto.response.AuthResponse;
import az.edu.msauth.dto.response.UserResponse;

public interface UserService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getProfile(Long userId);

    UserResponse updateProfile(Long userId, UpdateProfileRequest request);

    void changePassword(Long userId, ChangePasswordRequest request);

    void initiatePasswordReset(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void deleteUser(Long userId);
}
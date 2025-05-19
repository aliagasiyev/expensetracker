package az.edu.msauth.service;

import az.edu.msauth.dto.request.*;
import az.edu.msauth.dto.response.AuthResponse;
import az.edu.msauth.dto.response.UserResponse;
import az.edu.msauth.dto.response.AdminStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    // Authentication
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

    // User Profile
    UserResponse getProfile(Long userId);
    UserResponse updateProfile(Long userId, UpdateProfileRequest request);
    void changePassword(Long userId, ChangePasswordRequest request);

    // Password Reset
    void initiatePasswordReset(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);

    // User Management
    void deleteUser(Long userId);

    // Admin Operations
    Page<UserResponse> getAllUsers(String search, Pageable pageable);
    void blockUser(Long userId);
    void unblockUser(Long userId);
    AdminStatistics getAdminStatistics();
}
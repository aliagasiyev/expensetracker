package az.edu.msauth.service;

import az.edu.msauth.dto.request.*;
import az.edu.msauth.dto.response.AuthResponse;
import az.edu.msauth.dto.response.UserResponse;
import az.edu.msauth.dto.response.AdminStatistics;
import az.edu.msauth.dto.response.TokenValidationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    TokenValidationResponse validateToken(String token);

    UserResponse getProfile(Long userId);

    UserResponse updateProfile(Long userId, UpdateProfileRequest request);

    void changePassword(Long userId, ChangePasswordRequest request);

    void initiatePasswordReset(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void deleteUser(Long userId);

    Page<UserResponse> getAllUsers(String search, Pageable pageable);

    void blockUser(Long userId);

    void unblockUser(Long userId);

    AdminStatistics getAdminStatistics();
}
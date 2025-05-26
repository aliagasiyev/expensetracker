package az.edu.msauth.service.impl;

import az.edu.msauth.dto.request.*;
import az.edu.msauth.dto.response.AdminStatistics;
import az.edu.msauth.dto.response.AuthResponse;
import az.edu.msauth.dto.response.UserResponse;
import az.edu.msauth.dto.response.TokenValidationResponse;
import az.edu.msauth.entity.PasswordResetToken;
import az.edu.msauth.entity.User;
import az.edu.msauth.entity.UserRole;
import az.edu.msauth.exception.*;
import az.edu.msauth.mapper.UserMapper;
import az.edu.msauth.repository.PasswordResetTokenRepository;
import az.edu.msauth.repository.UserRepository;
import az.edu.msauth.security.JwtService;
import az.edu.msauth.service.EmailService;
import az.edu.msauth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }
        user.setActive(true);

        user = userRepository.save(user);
        String token = jwtService.generateToken(user);

        // Send welcome email
        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

        return AuthResponse.builder()
                .token(token)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new UserBlockedException("User account is blocked");
        }

        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public TokenValidationResponse validateToken(String token) {
        log.info("GELEN TOKEN: {}", token);
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String email = jwtService.extractUsername(token);
            log.info("TOKEN EMAIL: {}", email);
            User user = userRepository.findByEmail(email)
                    .orElse(null);

            if (user != null && jwtService.isTokenValid(token, user)) {
                log.info("TOKEN VALID: userId={}, email={}, role={}", user.getId(), user.getEmail(), user.getRole().name());
                return new TokenValidationResponse(true, user.getId(), user.getEmail(), user.getRole().name());
            }

            log.warn("TOKEN INVALID or USER NOT FOUND");
            return new TokenValidationResponse(false, null, null, null);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return new TokenValidationResponse(false, null, null, null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getProfile(Long userId) {
        User user = getUserById(userId);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getUserById(userId);

        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        userMapper.updateEntity(user, request);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = getUserById(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void initiatePasswordReset(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Delete any existing tokens
        tokenRepository.deleteByUser_Id(user.getId());

        // Create new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .used(false)
                .build();

        tokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired token"));

        if (resetToken.isExpired() || resetToken.isUsed()) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        resetToken.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(resetToken);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(String search, Pageable pageable) {
        Page<User> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userRepository.findByEmailContainingOrFullNameContaining(search, search, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        return users.map(userMapper::toResponse);
    }

    @Override
    public void blockUser(Long userId) {
        User user = getUserById(userId);
        if (user.getRole() == UserRole.ADMIN) {
            throw new InvalidCredentialsException("Cannot block admin users");
        }
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(Long userId) {
        User user = getUserById(userId);
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminStatistics getAdminStatistics() {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime firstDayOfMonth = today.withDayOfMonth(1);

        return AdminStatistics.builder()
                .totalUsers(userRepository.count())
                .activeUsers(userRepository.countByActiveTrue())
                .blockedUsers(userRepository.countByActiveFalse())
                .newUsersToday(userRepository.countByCreatedAtAfter(today))
                .newUsersThisMonth(userRepository.countByCreatedAtAfter(firstDayOfMonth))
                .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }
}
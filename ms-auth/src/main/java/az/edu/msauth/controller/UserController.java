package az.edu.msauth.controller;

import az.edu.msauth.dto.request.ChangePasswordRequest;
import az.edu.msauth.dto.request.UpdateProfileRequest;
import az.edu.msauth.dto.response.UserResponse;
import az.edu.msauth.security.SecurityUtils;
import az.edu.msauth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get current user profile")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        log.debug("getCurrentUser() method called");
        Long userId = SecurityUtils.getCurrentUserId();
        log.debug("Retrieved userId: {}", userId);
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @Operation(summary = "Get user by ID (Admin only)")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @Operation(summary = "Get all users (Admin only)")
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(search, pageable));
    }

    @Operation(summary = "Update profile")
    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    @Operation(summary = "Change password")
    @PutMapping("/change-password")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.changePassword(userId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete user account")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or (hasAuthority('ROLE_USER') and #id == authentication.principal.id)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete own account")
    @DeleteMapping("/me")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAccount() {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
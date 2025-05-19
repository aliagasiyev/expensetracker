package az.edu.msauth.controller;

import az.edu.msauth.dto.response.AdminStatistics;
import az.edu.msauth.dto.response.UserResponse;
import az.edu.msauth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Admin management APIs")
public class AdminController {

    private final UserService userService;

    @Operation(summary = "Get all users (paginated)")
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(search, pageable));
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Block user")
    @PutMapping("/users/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        userService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unblock user")
    @PutMapping("/users/{id}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable Long id) {
        userService.unblockUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get system statistics")
    @GetMapping("/statistics")
    public ResponseEntity<AdminStatistics> getStatistics() {
        return ResponseEntity.ok(userService.getAdminStatistics());
    }
}
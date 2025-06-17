package az.edu.msnotification.controller;

import az.edu.msnotification.dto.NotificationSettingsDto;
import az.edu.msnotification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/settings")
    public ResponseEntity<NotificationSettingsDto> createSettings(@RequestBody @Valid NotificationSettingsDto dto) {
        return ResponseEntity.ok(notificationService.createSettings(dto));
    }

    @GetMapping("/settings/{userId}")
    public ResponseEntity<NotificationSettingsDto> getSettings(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getSettings(userId));
    }

    @PutMapping("/settings/{userId}")
    public ResponseEntity<NotificationSettingsDto> updateSettings(@PathVariable Long userId, @RequestBody @Valid NotificationSettingsDto dto) {
        return ResponseEntity.ok(notificationService.updateSettings(userId, dto));
    }

    @PostMapping("/test-send/{userId}")
    public ResponseEntity<String> testSend(@PathVariable Long userId) {
        notificationService.sendTestNotification(userId);
        return ResponseEntity.ok("Test notification sent");
    }
}

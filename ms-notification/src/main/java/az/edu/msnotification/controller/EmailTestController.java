package az.edu.msnotification.controller;

import az.edu.msnotification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmailTestController {
    private final EmailService emailService;

    @PostMapping("/send-test-mail")
    public ResponseEntity<String> sendTestMail(@RequestParam String to) {
        emailService.sendEmail(to, "Test Mail", "This is a test email sent via Mailtrap SMTP.");
        return ResponseEntity.ok("Test mail sent to " + to);
    }
}

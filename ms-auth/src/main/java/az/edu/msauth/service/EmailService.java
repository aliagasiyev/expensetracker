package az.edu.msauth.service;

import java.util.Map;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);

    void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> templateModel);

    void sendPasswordResetEmail(String to, String resetToken);

    void sendWelcomeEmail(String to, String fullName);
}
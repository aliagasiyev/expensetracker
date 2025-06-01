package az.edu.msauth.service;

public interface EmailService {

    void sendPasswordResetEmail(String to, String resetToken);

    void sendWelcomeEmail(String to, String fullName);
}
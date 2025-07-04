package az.edu.msnotification.service;

import az.edu.msnotification.dto.NotificationSettingsDto;
import az.edu.msnotification.entity.NotificationSettings;
import az.edu.msnotification.mapper.NotificationSettingsMapper;
import az.edu.msnotification.repository.NotificationSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class NotificationService {
    private final NotificationSettingsRepository settingsRepository;
    private final JavaMailSender mailSender;
    private final NotificationSettingsMapper mapper;

    @Transactional
    public NotificationSettingsDto createSettings(NotificationSettingsDto dto) {
        NotificationSettings entity = mapper.toEntity(dto);
        settingsRepository.save(entity);
        return mapper.toDto(entity);
    }

    public NotificationSettingsDto getSettings(Long userId) {
        return settingsRepository.findById(userId)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Transactional
    public NotificationSettingsDto updateSettings(Long userId, NotificationSettingsDto dto) {
        NotificationSettings entity = settingsRepository.findById(userId).orElseThrow();
        entity.setEmail(dto.getEmail());
        entity.setEmailEnabled(dto.isEmailEnabled());
        entity.setInAppEnabled(dto.isInAppEnabled());
        settingsRepository.save(entity);
        return mapper.toDto(entity);
    }

    public void sendTestNotification(Long userId) {
        NotificationSettings settings = settingsRepository.findById(userId).orElse(null);
        if (settings != null && settings.isEmailEnabled()) {
            sendEmail(settings.getEmail(), "Test Notification", "This is a test notification.");
        }
        log.info("In-app test notification sent to user {}", userId);
    }

    @KafkaListener(topics = "expense.limit-exceeded", groupId = "notification-group")
    public void handleLimitExceeded(String message) {
        Long userId = Long.valueOf(message);
        NotificationSettings settings = settingsRepository.findById(userId).orElse(null);
        if (settings != null && settings.isEmailEnabled()) {
            sendEmail(settings.getEmail(), "Spending Limit Exceeded", "You have exceeded your spending limit.");
        }
        log.info("Limit-exceeded alert sent to user {}", userId);
    }

    @KafkaListener(topics = "monthly.expense.summary", groupId = "notification-group")
    public void handleMonthlySummary(String message) {
        Long userId = Long.valueOf(message);
        NotificationSettings settings = settingsRepository.findById(userId).orElse(null);
        if (settings != null && settings.isEmailEnabled()) {
            sendEmail(settings.getEmail(), "Monthly Summary", "Here is your monthly expense summary.");
        }
        log.info("Monthly summary sent to user {}", userId);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

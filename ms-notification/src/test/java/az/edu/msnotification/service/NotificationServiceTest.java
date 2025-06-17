package az.edu.msnotification.service;

import az.edu.msnotification.dto.NotificationSettingsDto;
import az.edu.msnotification.entity.NotificationSettings;
import az.edu.msnotification.mapper.NotificationSettingsMapper;
import az.edu.msnotification.repository.NotificationSettingsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {
    @Mock
    private NotificationSettingsRepository repository;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private NotificationSettingsMapper mapper;
    @InjectMocks
    private NotificationService service;

    NotificationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSettings() {
        NotificationSettingsDto dto = new NotificationSettingsDto();
        NotificationSettings entity = new NotificationSettings();
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);
        NotificationSettingsDto result = service.createSettings(dto);
        assertNotNull(result);
        verify(repository).save(entity);
    }
}

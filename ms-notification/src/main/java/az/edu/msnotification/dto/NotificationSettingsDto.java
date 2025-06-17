package az.edu.msnotification.dto;

import lombok.Data;

@Data
public class NotificationSettingsDto {
    private Long userId;
    private String email;
    private boolean emailEnabled;
    private boolean inAppEnabled;
}

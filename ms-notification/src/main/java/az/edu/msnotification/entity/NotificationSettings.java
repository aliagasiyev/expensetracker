package az.edu.msnotification.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSettings {
    @Id
    private Long userId;
    private String email;
    private boolean emailEnabled;
    private boolean inAppEnabled;
}

package az.edu.msnotification.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationSettingsTest {
    @Test
    void testNotificationSettingsCreationWithValidData() {
        NotificationSettings settings = NotificationSettings.builder()
                .userId(1L)
                .email("user@a.com")
                .emailEnabled(true)
                .inAppEnabled(false)
                .build();
        assertNotNull(settings);
        assertEquals(1L, settings.getUserId());
        assertEquals("user@a.com", settings.getEmail());
        assertTrue(settings.isEmailEnabled());
        assertFalse(settings.isInAppEnabled());
    }

    @Test
    void testNotificationSettingsCreationWithNullFields() {
        NotificationSettings settings = NotificationSettings.builder().build();
        assertNull(settings.getUserId());
        assertNull(settings.getEmail());
        assertFalse(settings.isEmailEnabled());
        assertFalse(settings.isInAppEnabled());
    }

    @Test
    void testNotificationSettingsUpdateFields() {
        NotificationSettings settings = NotificationSettings.builder().email("old@a.com").build();
        settings.setEmail("new@a.com");
        assertEquals("new@a.com", settings.getEmail());
    }

    @Test
    void testNotificationSettingsEqualsAndHashCode() {
        NotificationSettings s1 = NotificationSettings.builder().userId(1L).email("a@a.com").build();
        NotificationSettings s2 = NotificationSettings.builder().userId(1L).email("a@a.com").build();
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }
}

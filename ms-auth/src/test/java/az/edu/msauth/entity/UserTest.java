package az.edu.msauth.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void testUserCreationWithValidData() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .fullName("Test User")
                .role(UserRole.USER)
                .phoneNumber("1234567890")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build();
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getFullName());
        assertEquals(UserRole.USER, user.getRole());
        assertTrue(user.isActive());
    }

    @Test
    void testUserCreationWithNullFields() {
        User user = User.builder().build();
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getFullName());
        assertNull(user.getRole());
        assertNull(user.getPhoneNumber());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
        assertTrue(user.isActive()); // Default value
    }

    @Test
    void testUserUpdateFields() {
        User user = User.builder().email("old@example.com").build();
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void testUserEqualsAndHashCode() {
        User u1 = User.builder().id(1L).email("a@example.com").build();
        User u2 = User.builder().id(1L).email("a@example.com").build();
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testUserOnCreateSetsTimestamps() {
        User user = User.builder().build();
        user.onCreate();
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void testUserOnUpdateSetsUpdatedAt() {
        User user = User.builder().build();
        user.onUpdate();
        assertNotNull(user.getUpdatedAt());
    }
}

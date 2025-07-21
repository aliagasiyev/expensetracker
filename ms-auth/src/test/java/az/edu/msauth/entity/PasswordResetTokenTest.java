package az.edu.msauth.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResetTokenTest {
    @Test
    void testTokenCreationWithValidData() {
        User user = User.builder().id(1L).email("a@b.com").build();
        PasswordResetToken token = PasswordResetToken.builder()
                .id(1L)
                .token("abc123")
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .used(false)
                .build();
        assertNotNull(token);
        assertEquals("abc123", token.getToken());
        assertEquals(user, token.getUser());
        assertFalse(token.isUsed());
    }

    @Test
    void testTokenCreationWithNullFields() {
        PasswordResetToken token = PasswordResetToken.builder().build();
        assertNull(token.getToken());
        assertNull(token.getUser());
        assertNull(token.getExpiryDate());
        assertFalse(token.isUsed());
    }

    @Test
    void testTokenIsExpired() {
        PasswordResetToken token = PasswordResetToken.builder()
                .expiryDate(LocalDateTime.now().minusMinutes(1))
                .build();
        assertTrue(token.isExpired());
    }

    @Test
    void testTokenIsNotExpired() {
        PasswordResetToken token = PasswordResetToken.builder()
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();
        assertFalse(token.isExpired());
    }

    @Test
    void testOnCreateSetsExpiryDate() {
        PasswordResetToken token = PasswordResetToken.builder().build();
        token.onCreate();
        assertNotNull(token.getExpiryDate());
    }

    @Test
    void testTokenEqualsAndHashCode() {
        PasswordResetToken t1 = PasswordResetToken.builder().id(1L).token("abc").build();
        PasswordResetToken t2 = PasswordResetToken.builder().id(1L).token("abc").build();
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }
}

package az.edu.msauth.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private final Long userId;
    
    public JwtAuthenticationToken(UserDetails principal, Collection<? extends GrantedAuthority> authorities, Long userId) {
        super(principal, null, authorities);
        this.userId = userId;
        // Don't call setAuthenticated(true) - the parent constructor already handles this
    }
    
    public Long getUserId() {
        return userId;
    }
} 
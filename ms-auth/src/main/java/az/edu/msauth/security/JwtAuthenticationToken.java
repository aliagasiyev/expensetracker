package az.edu.msauth.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    
    private final Long userId;
    
    public JwtAuthenticationToken(UserDetails principal, Collection<? extends GrantedAuthority> authorities, Long userId) {
        super(principal, null, authorities);
        this.userId = userId;
    }

}
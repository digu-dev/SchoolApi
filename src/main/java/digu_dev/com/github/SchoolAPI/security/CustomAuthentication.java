package digu_dev.com.github.SchoolAPI.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import digu_dev.com.github.SchoolAPI.entity.UserEntity;
import lombok.Getter;

@Getter
public class CustomAuthentication implements Authentication {
   
    @Autowired
    UserEntity user;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
       
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    
}

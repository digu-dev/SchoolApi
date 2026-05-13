package digu_dev.com.github.SchoolAPI.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import digu_dev.com.github.SchoolAPI.entity.UserEntity;
import digu_dev.com.github.SchoolAPI.service.UserService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<UserEntity> userFound = userService.findByUsername(username);
        if (userFound.isPresent()) {
                UserEntity user = userFound.get();
                if (passwordEncoder.matches(password, user.getPassword())) {
                    CustomAuthentication customAuth = new CustomAuthentication();
                    customAuth.setAuthenticated(true);
                    return customAuth;
                } else {
                    throw new AuthenticationException("Invalid password") {};
                }
            } else {
                throw new AuthenticationException("User not found") {};
            }    
           
        
        
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

    
}

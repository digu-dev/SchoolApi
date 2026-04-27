package digu_dev.com.github.SchoolAPI.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import digu_dev.com.github.SchoolAPI.entity.UserEntity;
import digu_dev.com.github.SchoolAPI.repository.UserRepository;
import digu_dev.com.github.SchoolAPI.service.UserService;


public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;

    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        UserEntity userEntity = user.get();
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(userEntity.getRole())
                .build();
    }



    public CustomUserDetailsService(UserService userService) {
        //TODO Auto-generated constructor stub
    }

}

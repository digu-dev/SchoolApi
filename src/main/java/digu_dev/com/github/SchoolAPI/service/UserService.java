package digu_dev.com.github.SchoolAPI.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import digu_dev.com.github.SchoolAPI.dto.UserDto;
import digu_dev.com.github.SchoolAPI.entity.UserEntity;
import digu_dev.com.github.SchoolAPI.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserEntity create(UserDto userDto) {
        UserEntity user = userDto.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void update(UserEntity user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID must not be null for update.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void delete(UserEntity user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID must not be null for update.");
        }
        userRepository.delete(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}

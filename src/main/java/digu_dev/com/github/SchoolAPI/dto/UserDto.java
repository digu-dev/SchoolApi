package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
    @NotBlank 
    String username, 
    @NotBlank   
    String password,
    @NotBlank
    String role) {


        public UserEntity toEntity() {
            UserEntity user = new UserEntity();
            user.setUsername(this.username);
            user.setPassword(this.password);
            user.setRole(this.role);
            return user;
        }

}

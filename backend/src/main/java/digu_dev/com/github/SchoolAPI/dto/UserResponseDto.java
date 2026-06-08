package digu_dev.com.github.SchoolAPI.dto;

import java.util.List;

import digu_dev.com.github.SchoolAPI.entity.UserEntity;

public record UserResponseDto(Long id, String username, List<String> roles) {

    public static UserResponseDto fromEntity(UserEntity user) {
        return new UserResponseDto(user.getId(), user.getUsername(), List.copyOf(user.getRoles()));
    }
}

package digu_dev.com.github.SchoolAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubjectDto(Long id, 
    @NotBlank @Size(max = 100)
    String name) {

    public SubjectDto(String name) {
        this(null, name);
    }

}

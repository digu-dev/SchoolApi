package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.Subject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubjectDto(Long id, 
    @NotBlank @Size(max = 100)
    String name) {


    public Subject toEntity() {
        Subject subject = new Subject();
        subject.setId(this.id);
        subject.setName(this.name);
        return subject;
    }
}

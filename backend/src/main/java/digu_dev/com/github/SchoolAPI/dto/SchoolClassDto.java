package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.SchoolClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SchoolClassDto(Long id, @NotBlank @Size(max = 10) String classCode) {

    public SchoolClass toEntity() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(this.id);
        schoolClass.setClassCode(this.classCode);
        return schoolClass;
    }

}

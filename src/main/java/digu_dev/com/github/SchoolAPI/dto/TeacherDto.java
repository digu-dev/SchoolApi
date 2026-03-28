package digu_dev.com.github.SchoolAPI.dto;

import java.util.List;

import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.entity.Teacher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TeacherDto(Long id, 
    @NotBlank @Size(max = 100)
    String name, 
    @NotBlank @Size(max = 100)
    String email, 
    @NotEmpty
    List<String> subject) {

    public TeacherDto(Teacher teacher) {
        this(teacher.getId(),
        teacher.getName(),
        teacher.getEmail(),
        teacher.getSubjects().stream().map(Subject::getName).toList());
    }

}

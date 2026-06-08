package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StudentDto(Long id, 
    @NotBlank @Size(max = 100)
    String name, 
    @NotBlank @Size(max = 100)
    String registration, 
    @NotBlank @Size(max = 10) 
    String schoolClassCode) {

    public Student toEntity() {
        Student student = new Student();
        student.setId(this.id);
        student.setName(this.name);
        student.setRegistration(this.registration);
        return student;
    }

}

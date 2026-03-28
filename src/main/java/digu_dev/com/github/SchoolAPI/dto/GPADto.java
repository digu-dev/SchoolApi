package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.GPA;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GPADto(Long id, 
    @NotNull 
    @DecimalMin(value = "0.0", inclusive = true) 
    @DecimalMax(value = "100.0", inclusive = true) 
    Double finalGrade,
    @NotBlank @Size(max = 50)
    String subjectName, 
    @NotBlank @Size(max = 100)
    String studentName, 
    @NotBlank @Size(max = 15)
    String status){

    public GPADto(GPA gpa) {
        this(gpa.getId(), gpa.getFinalGrade(), gpa.getSubject().getName(), gpa.getStudent().getName(), gpa.getStatus().toString());
    }

}
package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.GPA;
import digu_dev.com.github.SchoolAPI.entity.StatusEnum;
import digu_dev.com.github.SchoolAPI.entity.Student;
import digu_dev.com.github.SchoolAPI.entity.Subject;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record GPADto(Long id, 
    @NotNull 
    @DecimalMin(value = "0.0", inclusive = true) 
    @DecimalMax(value = "30.0", inclusive = true) 
    Double g1,
    @NotNull 
    @DecimalMin(value = "0.0", inclusive = true) 
    @DecimalMax(value = "30.0", inclusive = true) 
    Double g2,
    @NotNull 
    @DecimalMin(value = "0.0", inclusive = true) 
    @DecimalMax(value = "40.0", inclusive = true) 
    Double g3,
    @NotNull 
    @DecimalMin(value = "0.0", inclusive = true) 
    @DecimalMax(value = "100.0", inclusive = true) 
    Double finalGrade,
    @NotNull
    Subject subject, 
    @NotNull
    Student student, 
    @NotNull
    StatusEnum status){


    
    public GPA toEntity() {
        GPA gpa = new GPA();
        gpa.setId(this.id);
        gpa.setGrade1(this.g1);
        gpa.setGrade2(this.g2);
        gpa.setGrade3(this.g3);
        gpa.setFinalGrade(this.finalGrade);
        gpa.setSubject(this.subject);
        gpa.setStudent(this.student);
        gpa.setStatus(this.status);
        return gpa;
    }

}
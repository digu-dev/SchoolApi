package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.GPA;

public record GPADto(Long id, Double finalGrade, String subjectName, String studentName, String status){

    public GPADto(GPA gpa) {
        this(gpa.getId(), gpa.getFinalGrade(), gpa.getSubject().getName(), gpa.getStudent().getName(), gpa.getStatus().toString());
    }

}
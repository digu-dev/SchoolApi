package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.Student;

public record SudentDto(Long id, String name, String registration, String schoolClassCode) {

    public SudentDto(Student student) {
        this(student.getId(), 
        student.getName(), 
        student.getRegistration(), 
        student.getSchoolClass().getClassCode());
    }

}

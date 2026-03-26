package digu_dev.com.github.SchoolAPI.dto;

import java.util.List;

import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.entity.Teacher;

public record TeacherDto(Long id, String name, String email, List<String> subject) {

    public TeacherDto(Teacher teacher) {
        this(teacher.getId(),
        teacher.getName(),
        teacher.getEmail(),
        teacher.getSubjects().stream().map(Subject::getName).toList());
    }

}

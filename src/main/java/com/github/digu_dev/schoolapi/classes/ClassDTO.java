package com.github.digu_dev.schoolapi.classes;

import com.github.digu_dev.schoolapi.classroom.ClassRoomEntity;
import com.github.digu_dev.schoolapi.subject.SubjectEntity;
import com.github.digu_dev.schoolapi.teacher.TeacherEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClassDTO (Long id,
                        @NotBlank(message = "Required field")
                        SubjectEntity subject,
                        @NotBlank(message = "Required field")
                        TeacherEntity teacher,
                        @NotBlank(message = "Required field")
                        ClassRoomEntity classRoom,
                        @NotNull(message = "Required field")
                        Integer classesPerWeek) {

    public ClassEntity mappedByClassEntity(){
        ClassEntity classEntity = new ClassEntity();
        classEntity.setSubject(this.subject);
        classEntity.setTeacher(this.teacher);
        classEntity.setClassRoom(this.classRoom);
        classEntity.setClassesPerWeek(this.classesPerWeek);
        return classEntity;
    }
}

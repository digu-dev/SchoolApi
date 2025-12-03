package com.github.digu_dev.schoolapi.teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TeacherDTO(@NotBlank(message = "Field Required!")
                         UUID id,
                         @NotBlank(message = "Field Required!")
                         String name,
                         @NotNull(message = "Field Required!")
                         Integer age,
                         @NotNull(message = "Field Required!")
                         Integer classesPerWeek) {

    public TeacherEntity mappedByTeacherEntity(){
        TeacherEntity teacher = new TeacherEntity();
        teacher.setName(this.name);
        teacher.setAge(this.age);
        teacher.setClassesPerWeek(this.classesPerWeek);

        return teacher;
    }
}

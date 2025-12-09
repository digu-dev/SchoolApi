package com.github.digu_dev.schoolapi.student;

import com.github.digu_dev.schoolapi.classroom.ClassRoomEntity;
import com.github.digu_dev.schoolapi.enums.Grade;
import com.github.digu_dev.schoolapi.enums.Period;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

public record StudentDTO(UUID id,
                         @NotBlank (message = "Required field")
                         String name,
                         @NotNull (message = "Required field")
                         Integer age,
                         @NotBlank (message = "Required field")
                         @PastOrPresent(message = "Birth date can't be from the future")
                         LocalDate birthDate,
                         @NotBlank (message = "Required field")
                         Grade grade,
                         @NotBlank (message = "Required field")
                         Period period,
                         ClassRoomEntity classRoom) {


    public StudentEntity mappedByStudentDTO (){
        StudentEntity student = new StudentEntity();
        student.setName(this.name);
        student.setAge(this.age);
        student.setBirthDate((this.birthDate));
        student.setGrade(this.grade);
        student.setPeriod(this.period);
        student.setClassRoom(this.classRoom);
        return student;
    }
}

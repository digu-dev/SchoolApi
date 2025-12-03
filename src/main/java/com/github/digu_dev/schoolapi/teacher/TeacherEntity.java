package com.github.digu_dev.schoolapi.teacher;

import com.github.digu_dev.schoolapi.classes.ClassEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString
@Table(name = "Teacher")
public class TeacherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private Integer age;

    private String educationLevel;

    private LocalDate birthDate;

    private Integer classesPerWeek;

    @OneToMany(mappedBy = "teacher")
    private List<ClassEntity> classes;
}

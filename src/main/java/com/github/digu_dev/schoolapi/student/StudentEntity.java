package com.github.digu_dev.schoolapi.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.digu_dev.schoolapi.classroom.ClassRoomEntity;
import com.github.digu_dev.schoolapi.enums.Grade;
import com.github.digu_dev.schoolapi.enums.Period;
import com.github.digu_dev.schoolapi.registration.RegistrationEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString
@Table(name = "Student")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private Integer age;

    private LocalDate birthDate;

    private String birthName;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    private Period period;

    @ManyToOne
    @JoinColumn(name = "classroom")
    private ClassRoomEntity classRoom;


    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<RegistrationEntity> registrations;
}

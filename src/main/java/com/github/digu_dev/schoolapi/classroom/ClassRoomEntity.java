package com.github.digu_dev.schoolapi.classroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.digu_dev.schoolapi.classes.ClassEntity;
import com.github.digu_dev.schoolapi.enums.Period;
import com.github.digu_dev.schoolapi.registration.RegistrationEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString
@Table(name = "ClassRoom")
public class ClassRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Enumerated
    private Period period;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<RegistrationEntity> registrations;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ClassEntity> classes;
}

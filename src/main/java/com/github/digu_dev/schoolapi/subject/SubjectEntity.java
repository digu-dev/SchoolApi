package com.github.digu_dev.schoolapi.subject;

import com.github.digu_dev.schoolapi.classes.ClassEntity;
import com.github.digu_dev.schoolapi.registration.RegistrationEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString
@Table(name = "Subject")
public class SubjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "subject")
    private List<RegistrationEntity> registration;

    @OneToMany(mappedBy = "subject")
    private List<ClassEntity> classes;
}

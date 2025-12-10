package com.github.digu_dev.schoolapi.subject;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<RegistrationEntity> registration;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ClassEntity> classes;
}

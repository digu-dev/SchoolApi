package com.github.digu_dev.schoolapi.grades;

import com.github.digu_dev.schoolapi.registration.RegistrationEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
public class GradesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer bimester;

    private Double grade;

    @ManyToOne
    @JoinColumn(name = "registration", nullable = false)
    private RegistrationEntity registration;
}

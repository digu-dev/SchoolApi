package com.github.digu_dev.schoolapi.registration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.digu_dev.schoolapi.classroom.ClassRoomEntity;
import com.github.digu_dev.schoolapi.grades.GradesEntity;
import com.github.digu_dev.schoolapi.student.StudentEntity;
import com.github.digu_dev.schoolapi.subject.SubjectEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString
@Table(name = "Registration")
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "student", nullable = false)
    
    private StudentEntity student;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject", nullable = false)
    private SubjectEntity subject;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "classRoom", nullable = false)
    private ClassRoomEntity classRoom;

    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<GradesEntity> grades;
}

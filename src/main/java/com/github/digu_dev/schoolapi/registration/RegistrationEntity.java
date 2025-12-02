package com.github.digu_dev.schoolapi.registration;

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
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @ManyToOne
    @JoinColumn(name = "student", nullable = false)
    private StudentEntity student;


    @ManyToOne
    @JoinColumn(name = "subject", nullable = false)
    private SubjectEntity subject;

    @ManyToOne
    @JoinColumn(name = "classRoom", nullable = false)
    private ClassRoomEntity classRoom;

    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GradesEntity> grades;
}

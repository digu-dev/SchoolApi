package com.github.digu_dev.schoolapi.classes;

import com.github.digu_dev.schoolapi.classroom.ClassRoomEntity;
import com.github.digu_dev.schoolapi.subject.SubjectEntity;
import com.github.digu_dev.schoolapi.teacher.TeacherEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "Class")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher", nullable = false)
    private TeacherEntity teacher;

    @ManyToOne
    @JoinColumn(name = "subject", nullable = false)
    private SubjectEntity subject;

    @ManyToOne
    @JoinColumn(name = "classroom", nullable = false)
    private ClassRoomEntity classRoom;

    private Integer classesPerWeek;
}

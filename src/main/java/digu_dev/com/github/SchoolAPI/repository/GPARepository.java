package digu_dev.com.github.SchoolAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import digu_dev.com.github.SchoolAPI.entity.GPA;

@Repository
public interface GPARepository extends JpaRepository<GPA, Long> {
    @Query("SELECT g FROM GPA g JOIN FETCH g.student s JOIN FETCH g.subject WHERE s.id = :studentId")
    List<GPA> findByStudentWithDetails(Long studentId);

    @Query("SELECT g FROM GPA g JOIN FETCH g.student s JOIN FETCH g.subject WHERE s.id = :studentId AND g.semester = :semester")
    List<GPA> findGradesBySubjectAndClass(Long subjectId, Long classId);

   @Query("SELECT g.finalGrade FROM GPA g WHERE g.student.id = :studentId AND g.subject.id = :subjectId")
    Double findFinalGradeByStudentAndSubject(Long studentId, Long subjectId);

    @Query("SELECT g.finalGrade FROM GPA g WHERE g.student.id = :studentId")
    List<Double> findAllFinalGradesByStudent(Long studentId);

}

package digu_dev.com.github.SchoolAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import digu_dev.com.github.SchoolAPI.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT DISTINCT t FROM Teacher t JOIN FETCH t.subjects WHERE t.id = :id")
    Optional<Teacher> findByIdWithSubjects(Long id);

    List<Teacher> findByDepartmentId(Long departmentId);

}

package digu_dev.com.github.SchoolAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import digu_dev.com.github.SchoolAPI.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("SELECT DISTINCT s FROM Subject s JOIN FETCH s.teacherSubject WHERE s.name = :name")
    Optional<Subject> findByNameWithTeachers(String name);

    @Query("SELECT DISTINCT s FROM Subject s WHERE s.name = :name")
    Optional<Subject> findByName(String name);

}

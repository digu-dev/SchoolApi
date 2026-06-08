package digu_dev.com.github.SchoolAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import digu_dev.com.github.SchoolAPI.entity.SchoolClass;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {

    @Query("SELECT DISTINCT sc FROM SchoolClass sc LEFT JOIN FETCH sc.students WHERE sc.classCode = :classCode")
    Optional<SchoolClass> findByCodeWithStudents(String classCode);

    @Query("SELECT sc FROM SchoolClass sc WHERE sc.classCode = :classCode")
    Optional<SchoolClass> findByClassCode(String classCode);
}

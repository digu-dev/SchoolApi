package digu_dev.com.github.SchoolAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import digu_dev.com.github.SchoolAPI.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

    @Query("SELECT DISTINCT s FROM Student s JOIN FETCH s.schoolClass c WHERE s.registration = :registration")
    Optional<Student> findByRegistrationWithClass (@Param("registration") String registration);

}

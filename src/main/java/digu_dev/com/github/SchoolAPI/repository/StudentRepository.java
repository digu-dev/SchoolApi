package digu_dev.com.github.SchoolAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import digu_dev.com.github.SchoolAPI.entity.Student;

@Repository
public interface StudentRepository {

    @Query("SELECT DISTINCT s FROM Student s JOIN FETCH s.classroom c WHERE s.registration = :registration")
    Optional<Student> findByRegistrationWithClass (String registration);

}

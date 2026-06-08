package digu_dev.com.github.SchoolAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import digu_dev.com.github.SchoolAPI.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{

    @Query("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.teachers WHERE d.id = :id")
    Optional<Department> findByIdWithTeachers(Long id);

    @Query("SELECT d FROM Department d WHERE d.name = :name")
    Optional<Department> findByName(String name);

}

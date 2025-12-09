package com.github.digu_dev.schoolapi.student;

import com.github.digu_dev.schoolapi.classes.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {
}

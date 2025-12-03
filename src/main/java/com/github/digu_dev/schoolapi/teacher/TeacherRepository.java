package com.github.digu_dev.schoolapi.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity, UUID> {
}

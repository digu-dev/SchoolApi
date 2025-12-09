package com.github.digu_dev.schoolapi.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public StudentEntity save(StudentDTO dto){
        StudentEntity student = new StudentEntity();
        student.setName(dto.name());;
        student.setAge(dto.age());
        student.setBirthDate(dto.birthDate());
        student.setGrade(dto.grade());
        student.setPeriod(dto.period());
        student.setClassRoom(dto.classRoom());
        return studentRepository.save(student);
    }

    public Optional<StudentEntity> findById(UUID id){ return studentRepository.findById(id);}

    public List<StudentEntity> findAll(){ return studentRepository.findAll();}

    public void updateById(StudentEntity student){
        if (student.getId() == null){
            throw new IllegalArgumentException("Student not found!");
        }
        studentRepository.save(student);
    }

    public void delete (StudentEntity student){
        if (student.getId() == null){
            throw new IllegalArgumentException("Student not found!");
        }
        studentRepository.delete(student);
    }
}

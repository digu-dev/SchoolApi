package com.github.digu_dev.schoolapi.teacher;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    public TeacherEntity save (TeacherDTO dto){

        TeacherEntity teacher = new TeacherEntity();

        teacher.setName(dto.name());
        teacher.setAge(dto.age());
        teacher.setClassesPerWeek(dto.classesPerWeek());

        return teacherRepository.save(teacher);
    }

    public TeacherEntity findById(UUID id){
        return teacherRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Teacher not found!"));
    }

    public List<TeacherEntity> findAll(){
        return teacherRepository.findAll();
    }
}

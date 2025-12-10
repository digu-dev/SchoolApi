package com.github.digu_dev.schoolapi.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    public TeacherEntity save (TeacherDTO dto){

        TeacherEntity teacher = new TeacherEntity();

        teacher.setName(dto.name());
        teacher.setAge(dto.age());


        return teacherRepository.save(teacher);
    }

    public Optional<TeacherEntity> findById(UUID id){
        return teacherRepository.findById(id);
    }

    public List<TeacherEntity> findAll(){
        return teacherRepository.findAll();
    }

    public void updateById (TeacherEntity teacher){
        if (teacher.getId() == null){
            throw new IllegalArgumentException("Teacher not found or not saved!");
        }
        teacherRepository.save(teacher);
    }

    public void delete (TeacherEntity teacher){
        if (teacher.getId() == null){
            throw new IllegalArgumentException("Teacher not found or not saved!");
        }
        teacherRepository.delete(teacher);
    }
}

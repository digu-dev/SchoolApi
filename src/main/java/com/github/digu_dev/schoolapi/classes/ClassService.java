package com.github.digu_dev.schoolapi.classes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassService {

    @Autowired
    ClassRepository classRepository;


    public ClassEntity save (ClassDTO dto){
       ClassEntity classEntity = new ClassEntity();
       classEntity.setSubject(dto.subject());
       classEntity.setClassRoom(dto.classRoom());
       classEntity.setTeacher(dto.teacher());
       classEntity.setClassesPerWeek(dto.classesPerWeek());
       return classRepository.save(classEntity);
    }

    public Optional<ClassEntity> findById (Long id){ return classRepository.findById(id); }

    public List<ClassEntity> findAll (){ return classRepository.findAll(); }

    public void updateById (ClassEntity classEntity){

        if (classEntity.getId() == null){
            throw new IllegalArgumentException("Class id not found");
        }
        classRepository.save(classEntity);
    }

    public void deleteById(ClassEntity classEntity){

        if (classEntity.getId() == null){
            throw new IllegalArgumentException("Class id not found");
        }
        classRepository.delete(classEntity);
    }
}

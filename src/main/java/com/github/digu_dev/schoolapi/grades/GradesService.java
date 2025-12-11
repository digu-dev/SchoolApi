package com.github.digu_dev.schoolapi.grades;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradesService {

    @Autowired
    GradesRepository gradesRepository;


    public GradesEntity save(GradesDTO dto) {
        GradesEntity grades = new GradesEntity();
        grades.setGrade(dto.grade());
        grades.setBimester(dto.bimester());
        grades.setRegistration(dto.registration());
        return gradesRepository.save(grades);
    }

    public Optional<GradesEntity> findById(Long id){ return gradesRepository.findById(id); }

    public List<GradesEntity> findAll(){ return gradesRepository.findAll(); }

    public void updateById (GradesEntity grades){
        if (grades.getId() == null){
            throw new IllegalArgumentException("Grades Id not found");
        }
        gradesRepository.save(grades);
    }

    public void deleteById (GradesEntity grades){
        if (grades.getId() == null){
            throw new IllegalArgumentException("Grades Id not found");
        }
        gradesRepository.delete(grades);
    }
}

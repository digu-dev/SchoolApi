package com.github.digu_dev.schoolapi.subject;

import com.github.digu_dev.schoolapi.teacher.TeacherEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    public SubjectEntity save (SubjectDTO dto){

        SubjectEntity subject = new SubjectEntity();

        subject.setName(dto.name());

        return subjectRepository.save(subject);
    }

    public Optional<SubjectEntity> findById (Long id){
        return subjectRepository.findById(id);
    }

    public List<SubjectEntity> findAll (){return subjectRepository.findAll();}

    public void updateById (SubjectEntity subject){
        if (subject.getId() == null){
            throw new IllegalArgumentException("Subject not found or not saved!");
        }
        subjectRepository.save(subject);
    }

    public void delete (SubjectEntity subject){
        if (subject.getId() == null){
            throw new IllegalArgumentException("Subject Id not found");
        }
        subjectRepository.delete(subject);
    }
}

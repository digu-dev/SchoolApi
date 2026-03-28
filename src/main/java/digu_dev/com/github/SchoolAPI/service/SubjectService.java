package digu_dev.com.github.SchoolAPI.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import digu_dev.com.github.SchoolAPI.dto.SubjectDto;
import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.repository.SubjectRepository;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    @Transactional(rollbackFor = Exception.class)
    public Subject create (SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setName(subjectDto.name());
        return subjectRepository.save(subject);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Subject subject){
        if (subject.getId() == null) {
            throw new IllegalArgumentException("Subject ID must not be null for update.");
        }
        subjectRepository.save(subject);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Subject subject){
        if (subject.getId() == null) {
            throw new IllegalArgumentException("Subject ID must not be null for update.");
        }
        subjectRepository.delete(subject);
    }

    public Optional<Subject> findById (Long id) {
        return subjectRepository.findById(id);
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> findByNameWithTeachers(String name) {
        return subjectRepository.findByNameWithTeachers(name);
    }

    public Optional<Subject> findByName(String name) {
        return subjectRepository.findByName(name);
    }

}

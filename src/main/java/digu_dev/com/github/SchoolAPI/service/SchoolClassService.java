package digu_dev.com.github.SchoolAPI.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import digu_dev.com.github.SchoolAPI.dto.SchoolClassDto;
import digu_dev.com.github.SchoolAPI.entity.SchoolClass;
import digu_dev.com.github.SchoolAPI.repository.SchoolClassRepository;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class SchoolClassService {

    @Autowired
    SchoolClassRepository schoolClassRepository;

    @Transactional(rollbackFor = Exception.class)
    public SchoolClass create (SchoolClassDto schoolClassDto) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setClassCode(schoolClassDto.classCode());
        return schoolClassRepository.save(schoolClass);
    }   

    @Transactional(rollbackFor = Exception.class)
    public void update(SchoolClass schoolClass) {
        if(schoolClass.getId() == null) {
            throw new IllegalArgumentException("SchoolClass ID must not be null for update.");
        }
        schoolClassRepository.save(schoolClass);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(SchoolClass schoolClass) {
        if(schoolClass.getId() == null) {
            throw new IllegalArgumentException("SchoolClass ID must not be null for deletion.");
        }
        schoolClassRepository.delete(schoolClass);
    }

    public Optional<SchoolClass> findById(Long id) {
        return schoolClassRepository.findById(id);
    }

    public List<SchoolClass> findAll() {
        return schoolClassRepository.findAll();
    }

    public Optional<SchoolClass> findByClassCode(String classCode) {
        return schoolClassRepository.findByClassCode(classCode);
    }

    public Optional<SchoolClass> findByCodeWithStudents(String classCode) {
        return schoolClassRepository.findByCodeWithStudents(classCode);
    }

}

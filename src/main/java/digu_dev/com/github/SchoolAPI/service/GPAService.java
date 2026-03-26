package digu_dev.com.github.SchoolAPI.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import digu_dev.com.github.SchoolAPI.dto.GPADto;
import digu_dev.com.github.SchoolAPI.entity.GPA;
import digu_dev.com.github.SchoolAPI.repository.GPARepository;

@Service
@Transactional(readOnly = true, rollbackFor=Exception.class)
public class GPAService {

    @Autowired
    GPARepository gpaRepository;


    @Transactional(rollbackFor=Exception.class)
    public GPADto createGPA(Long id, Double g1, Double g2, Double g3) {
        GPA entity = gpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("grade record not found for id: " + id));
        
        entity.setGrade1(g1);
        entity.setGrade2(g2);
        entity.setGrade3(g3);
        
        // A média pode ser calculada aqui ou num método dentro da própria Entity
        Double finalGrade = (g1 + g2 + g3);
        entity.setFinalGrade(finalGrade);
        
        entity = gpaRepository.save(entity);
        return new GPADto(entity);
    }

    @Transactional(rollbackFor=Exception.class)
    public void update (GPA gpa){
        if (gpa.getId() == null) {
            throw new IllegalArgumentException("GPA ID cannot be null for update.");
        }
        gpaRepository.save(gpa);
    }

    @Transactional(rollbackFor=Exception.class)
    public void delete(GPA gpa){
        if(gpa.getId() == null) {
            throw new IllegalArgumentException("GPA ID cannot be null for deletion.");
        }
        gpaRepository.deleteById(gpa.getId());
    }

    public Optional<GPA> findById(Long id){
        return gpaRepository.findById(id);                
    }

    public List<GPA> findAll(){
        return gpaRepository.findAll();
    }   

    public List<GPA> findByStudentWithDetails(Long studentId) {
        return gpaRepository.findByStudentWithDetails(studentId);
    }

    public Double findFinalGradeByStudentAndSubject(Long studentId, Long subjectId) {
        return gpaRepository.findFinalGradeByStudentAndSubject(studentId, subjectId);
    }

    public List<Double> findAllFinalGradesByStudent(Long studentId) {
        return gpaRepository.findAllFinalGradesByStudent(studentId);
    }


}

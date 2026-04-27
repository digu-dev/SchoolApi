package digu_dev.com.github.SchoolAPI.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import digu_dev.com.github.SchoolAPI.dto.GPADto;
import digu_dev.com.github.SchoolAPI.entity.GPA;
import digu_dev.com.github.SchoolAPI.repository.GPARepository;

@Service
@Transactional(readOnly = true, rollbackFor=Exception.class)
@PreAuthorize("hasRole('TEACHER')")
public class GPAService {

    @Autowired
    GPARepository gpaRepository;


    public Double calculateFinalGrade(Double g1, Double g2, Double g3) {
        if (g1 == null || g2 == null || g3 == null) {
            throw new IllegalArgumentException("All grade components must be provided.");
        }
        return g1 + g2 + g3;
    }

    @Transactional(rollbackFor=Exception.class)
    public GPA createGPA(GPADto dto){ 

        
        GPA gpa = new GPA();
        gpa.setGrade1(dto.g1());
        gpa.setGrade2(dto.g2());
        gpa.setGrade3(dto.g3());
        gpa.setFinalGrade(calculateFinalGrade(dto.g1(), dto.g2(), dto.g3()));
        gpa.setStudent(dto.student());
        gpa.setSubject(dto.subject());
        gpa.setStatus(dto.status());
        return gpaRepository.save(gpa);
        
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

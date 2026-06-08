package digu_dev.com.github.SchoolAPI.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import digu_dev.com.github.SchoolAPI.dto.GPADto;
import digu_dev.com.github.SchoolAPI.entity.GPA;
import digu_dev.com.github.SchoolAPI.exception.ResourceAlreadyExistsException;
import digu_dev.com.github.SchoolAPI.service.GPAService;

@RestController
@RequestMapping("/gpa")
public class GPAController {

    @Autowired
    GPAService gpaService;

    @PostMapping
    public ResponseEntity<Object> createGPA(@RequestBody GPADto dto) {
        try {
            GPA createdGPA = dto.toEntity();
            gpaService.createGPA(dto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdGPA.getId()).toUri();
            return ResponseEntity.created(location).build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGPA(@PathVariable("id") Long id, @RequestBody GPADto dto) {
        try {
            Optional<GPA> existingGPA = gpaService.findById(id);
            if (existingGPA.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                GPA gpa = existingGPA.get();
                gpa.setGrade1(dto.g1());
                gpa.setGrade2(dto.g2());
                gpa.setGrade3(dto.g3());
                gpa.setFinalGrade(gpaService.calculateFinalGrade(dto.g1(), dto.g2(), dto.g3()));
                gpa.setStudent(dto.student());
                gpa.setSubject(dto.subject());
                gpa.setStatus(dto.status());
                gpaService.update(gpa);
                return ResponseEntity.noContent().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGPA(@PathVariable("id") Long id) {

        Optional<GPA> existingGPA = gpaService.findById(id);
        if (existingGPA.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            gpaService.delete(existingGPA.get());
            return ResponseEntity.noContent().build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<GPADto> findGPAById(@PathVariable("id") Long id) {

        Optional<GPA> existingGPA = gpaService.findById(id);

        if (existingGPA.isPresent()) {
            GPA gpa = existingGPA.get();
            GPADto dto = new GPADto(gpa.getId(), 
            gpa.getGrade1(), 
            gpa.getGrade2(), 
            gpa.getGrade3(),    
            gpa.getFinalGrade(), 
            gpa.getSubject(), 
            gpa.getStudent(), 
            gpa.getStatus());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<GPA>> findAll() {
        List<GPA> gpas = gpaService.findAll();
        if (gpas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
        return ResponseEntity.ok(gpas);
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GPA>> findByStudentWithDetails(@PathVariable("studentId") Long studentId) {
        List<GPA> gpas = gpaService.findByStudentWithDetails(studentId);
        if (gpas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
        return ResponseEntity.ok(gpas);
        }
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}/final-grade")
    public ResponseEntity<Double> findFinalGradeByStudentAndSubject(
            @PathVariable("studentId") Long studentId,
            @PathVariable("subjectId") Long subjectId) {
        Double finalGrade = gpaService.findFinalGradeByStudentAndSubject(studentId, subjectId);
        if (finalGrade == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(finalGrade);
        }
    }

    @GetMapping("/student/{studentId}/final-grades")
    public ResponseEntity<List<Double>> findAllFinalGradesByStudent(@PathVariable("studentId") Long studentId) {
        List<Double> finalGrades = gpaService.findAllFinalGradesByStudent(studentId);
        if (finalGrades.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(finalGrades);
        }
    }

}
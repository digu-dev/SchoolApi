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

import digu_dev.com.github.SchoolAPI.dto.SubjectDto;
import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.exception.ResourceAlreadyExistsException;
import digu_dev.com.github.SchoolAPI.service.SubjectService;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @PostMapping
    public ResponseEntity<Object> createSubject(@RequestBody SubjectDto subjectDto) {
        try {
            Subject subject = subjectDto.toEntity();
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(subject.getId()).toUri();
            Subject createdSubject = subjectService.create(subjectDto);
            return ResponseEntity.created(location).body(createdSubject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSubject(@PathVariable("id") Long id, @RequestBody Subject subject) {
        try {
            
            Optional<Subject> existingSubject = subjectService.findById(id);
            if (existingSubject.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                Subject existing = existingSubject.get();
                existing.setName(subject.getName());
                subjectService.update(existing);
                return ResponseEntity.ok().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSubject(@PathVariable("id") Long id) {
        Optional<Subject> existingSubject = subjectService.findById(id);
        if (existingSubject.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            subjectService.delete(existingSubject.get());
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getSubjectById(@PathVariable("id") Long id) {
        Optional<Subject> existingSubject = subjectService.findById(id);
        if (existingSubject.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Subject subject = existingSubject.get();
            SubjectDto dto = new SubjectDto(subject.getId(), subject.getName());
            return ResponseEntity.ok(dto);
        }
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.findAll();
        if(subjects.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Subject> getSubjectByNameWithTeachers(@PathVariable("name") String name) {
        Optional<Subject> existingSubject = subjectService.findByNameWithTeachers(name);
        if (existingSubject.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(existingSubject.get());
        }
    }

    @GetMapping("/nameSubject/{name}")
    public ResponseEntity<Subject> getSubjectByName(@PathVariable("name") String name) {
        Optional<Subject> existingSubject = subjectService.findByName(name);
        if (existingSubject.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(existingSubject.get());
        }
    }

    
    
}

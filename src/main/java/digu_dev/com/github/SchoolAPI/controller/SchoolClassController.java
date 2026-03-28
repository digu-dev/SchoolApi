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

import digu_dev.com.github.SchoolAPI.dto.SchoolClassDto;
import digu_dev.com.github.SchoolAPI.entity.SchoolClass;
import digu_dev.com.github.SchoolAPI.exception.ResourceAlreadyExistsException;
import digu_dev.com.github.SchoolAPI.service.SchoolClassService;

@RestController 
@RequestMapping("/school-classes")
public class SchoolClassController {

    @Autowired
    SchoolClassService schoolClassService; 


    @PostMapping
    public ResponseEntity<Object> createSchoolClass(@RequestBody SchoolClassDto dto) {
        try {
            SchoolClass createdSchoolClass = dto.toEntity();
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdSchoolClass.getId()).toUri();
            schoolClassService.create(dto);
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
         }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSchoolClass(@PathVariable("id") Long id, @RequestBody SchoolClassDto dto) {
        try {
            Optional<SchoolClass> existingSchoolClass = schoolClassService.findById(id);
            if (existingSchoolClass.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                SchoolClass schoolClass = existingSchoolClass.get();
                schoolClass.setClassCode(dto.classCode());
                schoolClassService.update(schoolClass);
                return ResponseEntity.ok().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSchoolClass(@PathVariable("id") Long id) {
        
        Optional<SchoolClass> existingSchoolClass = schoolClassService.findById(id);
        if (existingSchoolClass.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            schoolClassService.delete(existingSchoolClass.get());
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolClassDto> getSchoolClassById(@PathVariable("id") Long id) {
        Optional<SchoolClass> existingSchoolClass = schoolClassService.findById(id);
        if (existingSchoolClass.isPresent()) {
            SchoolClass schoolClass = existingSchoolClass.get();
            SchoolClassDto dto = new SchoolClassDto(schoolClass.getId(), schoolClass.getClassCode());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
        
    }

    @GetMapping
    public ResponseEntity<List<SchoolClass>> getAllSchoolClasses() {
        List<SchoolClass> schoolClasses = schoolClassService.findAll();
       if (schoolClasses.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(schoolClasses);
        }
        
    }

    @GetMapping("/code/{classCode}")
    public ResponseEntity<SchoolClassDto> getSchoolClassByCode(@PathVariable("classCode") String classCode) {
        Optional<SchoolClass> existingSchoolClass = schoolClassService.findByClassCode(classCode);
        if (existingSchoolClass.isPresent()) {
            SchoolClass schoolClass = existingSchoolClass.get();
            SchoolClassDto dto = new SchoolClassDto(schoolClass.getId(), schoolClass.getClassCode());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/code/{classCode}/students")
    public ResponseEntity<SchoolClass> findByCodeWithStudents(@PathVariable("classCode") String classCode) {
        Optional<SchoolClass> existingSchoolClass = schoolClassService.findByCodeWithStudents(classCode);
        if (existingSchoolClass.isPresent()) {
            return ResponseEntity.ok(existingSchoolClass.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
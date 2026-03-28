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

import digu_dev.com.github.SchoolAPI.dto.TeacherDto;
import digu_dev.com.github.SchoolAPI.entity.Teacher;
import digu_dev.com.github.SchoolAPI.exception.ResourceAlreadyExistsException;
import digu_dev.com.github.SchoolAPI.service.TeacherService;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @PostMapping
    public ResponseEntity<Object> createTeacher(@RequestBody TeacherDto teacherDto) {
        try {
            Teacher teacher = teacherDto.toEntity();
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(teacher.getId()).toUri();
            Teacher createdTeacher = teacherService.create(teacherDto);
            return ResponseEntity.created(location).body(createdTeacher);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTeacher(@PathVariable("id") Long id, @RequestBody Teacher teacher) {
        try {
            
            Optional<Teacher> existingTeacher = teacherService.findById(id);
            if (existingTeacher.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                Teacher existing = existingTeacher.get();
                existing.setName(teacher.getName());
                existing.setEmail(teacher.getEmail());
                teacherService.update(existing);
                return ResponseEntity.ok().build();
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTeacher(@PathVariable("id") Long id) {
        Optional<Teacher> existingTeacher = teacherService.findById(id);
        if (existingTeacher.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            teacherService.delete(existingTeacher.get());
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable("id") Long id) {
        Optional<Teacher> existingTeacher = teacherService.findById(id);
        if (existingTeacher.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Teacher teacher = existingTeacher.get();
            TeacherDto teacherDto = new TeacherDto(teacher.getId(), teacher.getName(), teacher.getEmail(), teacher.getSubjects().stream().map(s -> s.getName()).toList());
            return ResponseEntity.ok(teacherDto);
        }
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.findAll();
        if (teachers.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(teachers);
        }
    }

    @GetMapping("/{id}/subjects")
    public ResponseEntity<List<String>> getTeacherSubjects(@PathVariable("id") Long id) {
        Optional<Teacher> existingTeacher = teacherService.findByIdWithSubjects(id);
        if (existingTeacher.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Teacher teacher = existingTeacher.get();
            List<String> subjects = teacher.getSubjects().stream().map(s -> s.getName()).toList();
            if(subjects.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(subjects);
        }
    }

    @GetMapping("/{id}/department")
    public ResponseEntity<String> getTeacherDepartment(@PathVariable("id") Long id) {
        Optional<Teacher> existingTeacher = teacherService.findByIdWithDepartment(id);
        if (existingTeacher.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Teacher teacher = existingTeacher.get();
            if(teacher.getDepartment() == null){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(teacher.getDepartment().getName());
        }
    }

    @GetMapping("/department/{name}")
     public ResponseEntity<List<Teacher>> getTeachersByDepartmentName(@PathVariable("name") String departmentName) {
        List<Teacher> teachers = teacherService.findByDepartmentName(departmentName);
        if (teachers.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(teachers);
        }
    }

}

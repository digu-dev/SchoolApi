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

import digu_dev.com.github.SchoolAPI.dto.StudentDto;
import digu_dev.com.github.SchoolAPI.entity.Student;
import digu_dev.com.github.SchoolAPI.exception.ResourceAlreadyExistsException;
import digu_dev.com.github.SchoolAPI.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping
    public ResponseEntity<Object> createStudent(@RequestBody StudentDto studentDto) {
        try {
            Student student = studentDto.toEntity();
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(student.getId()).toUri();  
            studentService.create(studentDto);
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable("id") Long id, @RequestBody Student student) {
        try{
            Optional<Student> existingStudent = studentService.findById(id);
            if (existingStudent.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                Student existing = existingStudent.get();
                existing.setName(student.getName());
                existing.setRegistration(student.getRegistration());
            studentService.update(existing);
            return ResponseEntity.ok().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable("id") Long id) {
        Optional<Student> existingStudent = studentService.findById(id);
        if (existingStudent.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            studentService.delete(existingStudent.get());
            return ResponseEntity.ok().build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Long id){

        Optional<Student> existingStudent = studentService.findById(id);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            StudentDto dto = new StudentDto(student.getId(), student.getName(), student.getRegistration(), student.getSchoolClass().getClassCode());
             return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.findAll();
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
        return ResponseEntity.ok(students);
        }
    }


    @GetMapping("/registration/{registration}")
    public ResponseEntity<StudentDto> getStudentByRegistration(@PathVariable("registration") String registration) {

        Optional<Student> existingStudent = studentService.findByRegistrationWithClass(registration);
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            StudentDto dto = new StudentDto(student.getId(), student.getName(), student.getRegistration(), student.getSchoolClass().getClassCode());
             return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

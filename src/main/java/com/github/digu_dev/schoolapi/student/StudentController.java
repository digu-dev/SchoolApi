package com.github.digu_dev.schoolapi.student;

import com.github.digu_dev.schoolapi.exceptions.DuplicatedRegisterException;
import com.github.digu_dev.schoolapi.exceptions.ResponseError;
import com.github.digu_dev.schoolapi.subject.SubjectDTO;
import com.github.digu_dev.schoolapi.subject.SubjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping
    public ResponseEntity<Object> save (@RequestBody StudentDTO dto){
        try{
            StudentEntity student = dto.mappedByStudentDTO();
            studentService.save(dto);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(student.getId()).toUri();
            return ResponseEntity.created(location).build();
        }catch (DuplicatedRegisterException e){
            ResponseError conflict = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(conflict.status()).body(conflict);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> findById(@PathVariable("id") String id){
        var studentId = UUID.fromString(id);
        Optional<StudentEntity> studentEntityOptional = studentService.findById(studentId);
        if (studentEntityOptional.isPresent()){
            StudentEntity student = studentEntityOptional.get();
            StudentDTO studentDTO = new StudentDTO(student.getId(),
                    student.getName(),student.getAge(),
                    student.getBirthDate(),
                    student.getGrade(),
                    student.getPeriod(),
                    student.getClassRoom());
            return ResponseEntity.ok(studentDTO);
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping
    public ResponseEntity<List<StudentEntity>> findAll(){
        List<StudentEntity> students = studentService.findAll();
        if (students.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateById (@PathVariable("id") String id, @RequestBody StudentDTO dto){
       try{
           var studentId = UUID.fromString(id);
           Optional<StudentEntity> studentEntityOptional = studentService.findById(studentId);
           if (studentEntityOptional.isEmpty()){
               return ResponseEntity.notFound().build();
           }else{
               var student = studentEntityOptional.get();
               student.setName(dto.name());
               student.setAge(dto.age());
               student.setBirthDate(dto.birthDate());
               student.setGrade(dto.grade());
               student.setPeriod(dto.period());
               student.setClassRoom(dto.classRoom());

               studentService.updateById(student);
               return ResponseEntity.noContent().build();
           }
       }catch (DuplicatedRegisterException e) {
           ResponseError conflict = ResponseError.conflict(e.getMessage());
           return ResponseEntity.status(conflict.status()).body(conflict);
       }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById (@PathVariable("id") String id){
        var studentId = UUID.fromString(id);
        Optional<StudentEntity> studentEntityOptional = studentService.findById(studentId);
        if (studentEntityOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        studentService.delete(studentEntityOptional.get());
        return ResponseEntity.noContent().build();
    }

}

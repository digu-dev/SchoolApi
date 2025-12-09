package com.github.digu_dev.schoolapi.student;

import com.github.digu_dev.schoolapi.exceptions.DuplicatedRegisterException;
import com.github.digu_dev.schoolapi.exceptions.ResponseError;
import com.github.digu_dev.schoolapi.subject.SubjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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



}

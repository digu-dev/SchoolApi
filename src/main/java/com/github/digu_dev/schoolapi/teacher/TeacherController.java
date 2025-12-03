package com.github.digu_dev.schoolapi.teacher;

import com.github.digu_dev.schoolapi.exceptions.DuplicatedRegisterException;
import com.github.digu_dev.schoolapi.exceptions.ResponseError;
import com.github.digu_dev.schoolapi.grades.GradesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @PostMapping
    public ResponseEntity<Object> save (@RequestBody TeacherDTO dto){
        try{
            TeacherEntity teacherEntity = dto.mappedByTeacherEntity();
            teacherService.save(dto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(teacherEntity.getId()).toUri();

            return ResponseEntity.created(location).build();
        }catch (DuplicatedRegisterException e){
            var dtoError = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(dtoError.status()).body(dtoError);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherEntity> findById(@PathVariable("id") String id){
        UUID teacherId = UUID.fromString(id);
        
    }
}

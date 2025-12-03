package com.github.digu_dev.schoolapi.teacher;

import com.github.digu_dev.schoolapi.exceptions.DuplicatedRegisterException;
import com.github.digu_dev.schoolapi.exceptions.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
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
    public ResponseEntity<TeacherDTO> findById(@PathVariable("id") String id){
        var teacherId = UUID.fromString(id);
        Optional<TeacherEntity> teacherEntityOptional = teacherService.findById(teacherId);
        boolean present = teacherEntityOptional.isPresent();
        if (present){
            TeacherEntity teacher = teacherEntityOptional.get();
            TeacherDTO teacherDTO = new TeacherDTO(teacher.getId(),
                    teacher.getName(),
                    teacher.getAge(),
                    teacher.getClassesPerWeek());
            return ResponseEntity.ok(teacherDTO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TeacherEntity>> findAll(){
        List<TeacherEntity> teachers = teacherService.findAll();
        if (teachers.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(teachers);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateById(@PathVariable("id") String id,@RequestBody TeacherDTO dto) {
        try{
            var teacherId = UUID.fromString(id);
            Optional<TeacherEntity> teacherEntityOptionalOptional = teacherService.findById(teacherId);
            if (teacherEntityOptionalOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var teacher = teacherEntityOptionalOptional.get();
            teacher.setName(dto.name());
            teacher.setAge(dto.age());
            teacher.setClassesPerWeek(dto.classesPerWeek());

            teacherService.updateById(teacher);

            return ResponseEntity.noContent().build();

        }catch(DuplicatedRegisterException e){
            var dtoError = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(dtoError.status()).body(dtoError);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void>  delete (@PathVariable("id") String id){
        UUID teacherId = UUID.fromString(id);
        Optional<TeacherEntity> teacherEntityOptional = teacherService.findById(teacherId);
        if(teacherEntityOptional.isPresent()){
            teacherService.delete(teacherEntityOptional.get());
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }

    }
}

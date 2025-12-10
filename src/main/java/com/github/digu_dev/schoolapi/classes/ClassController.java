package com.github.digu_dev.schoolapi.classes;

import com.github.digu_dev.schoolapi.exceptions.DuplicatedRegisterException;
import com.github.digu_dev.schoolapi.exceptions.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    ClassService classService;

    @PostMapping
    public ResponseEntity<Object> save (@RequestBody ClassDTO dto){
        try{
            ClassEntity classEntity = dto.mappedByClassEntity();
            classService.save(dto);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(classEntity.getId()).toUri();
            return ResponseEntity.created(location).build();
        }catch (DuplicatedRegisterException e) {
            ResponseError conflict = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(conflict.status()).body(conflict);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassDTO> findById (@PathVariable("id") Long id){
        Optional<ClassEntity> classEntityOptional = classService.findById(id);
        if (classEntityOptional.isPresent()){
            ClassEntity classEntity = classEntityOptional.get();
            ClassDTO dto = new ClassDTO(classEntity.getId(),
                    classEntity.getSubject(),
                    classEntity.getTeacher(),
                    classEntity.getClassRoom(),
                    classEntity.getClassesPerWeek());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ClassEntity>> findAll(){
        List<ClassEntity> classes = classService.findAll();
        if (classes.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(classes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateById(@PathVariable("id") Long id ,@RequestBody ClassDTO dto){
        try{
            Optional<ClassEntity> classEntityOptional = classService.findById(id);
            if (classEntityOptional.isPresent()){
                var classEntity = classEntityOptional.get();
                classEntity.setSubject(dto.subject());
                classEntity.setTeacher(dto.teacher());
                classEntity.setClassRoom(dto.classRoom());
                classEntity.setClassesPerWeek(dto.classesPerWeek());
                classService.updateById(classEntity);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }catch (DuplicatedRegisterException e){
            ResponseError conflict = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(conflict.status()).body(conflict);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id){
        Optional<ClassEntity> classEntityOptional = classService.findById(id);
        if (classEntityOptional.isPresent()){
            classService.deleteById(classEntityOptional.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}



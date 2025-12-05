package com.github.digu_dev.schoolapi.subject;

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
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @PostMapping
    public ResponseEntity<Object> save (@RequestBody SubjectDTO dto){
        try{
            SubjectEntity subjectEntity = dto.mappedBySubjectEntity();
            subjectService.save(dto);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(subjectEntity.getId()).toUri();
            return ResponseEntity.created(location).build();
        }catch (DuplicatedRegisterException e){
            var conflict = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(conflict.status()).body(conflict);

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> findById(@PathVariable("id") Long id){
        Optional<SubjectEntity> subjectEntityOptional = subjectService.findById(id);
        if(subjectEntityOptional.isPresent()){
            SubjectEntity subject = subjectEntityOptional.get();
            SubjectDTO subjectDTO = new SubjectDTO(subject.getId(), subject.getName());
            return ResponseEntity.ok(subjectDTO);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SubjectEntity>> findAll(){
        List<SubjectEntity> subjects = subjectService.findAll();
        if(subjects.isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(subjects);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateById (@PathVariable("id")Long id, @RequestBody SubjectDTO dto){
        try{
            Optional<SubjectEntity> subjectEntityOptional = subjectService.findById(id);
            if(subjectEntityOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            var subject = subjectEntityOptional.get();
            subject.setName(dto.name());
            subjectService.updateById(subject);

            return ResponseEntity.noContent().build();
        }catch (DuplicatedRegisterException e){
            ResponseError conflict = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(conflict.status()).body(conflict);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        Optional<SubjectEntity> subjectEntityOptional = subjectService.findById(id);
        if(subjectEntityOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            subjectService.delete(subjectEntityOptional.get());
            return ResponseEntity.noContent().build();
        }
    }
}

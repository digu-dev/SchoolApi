package com.github.digu_dev.schoolapi.grades;

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
@RequestMapping("/grades")
public class GradesController {

    @Autowired
    GradesService gradesService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody GradesDTO gradesDTO){
        try{
            GradesEntity gradesEntity = gradesDTO.mappedByGradesEntity();
            gradesService.save(gradesDTO);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(gradesEntity.getId()).toUri();

            return ResponseEntity.created(location).build();
        }catch (DuplicatedRegisterException e){
            var dtoError = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(dtoError.status()).body(dtoError);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradesDTO> findById (@PathVariable("id") Long id){
        Optional<GradesEntity> gradesEntityOptional = gradesService.findById(id);
        if(gradesEntityOptional.isPresent()){
            GradesEntity grades = gradesEntityOptional.get();
            GradesDTO gradesDTO = new GradesDTO(grades.getId(),
                    grades.getBimester(), grades.getGrade(), grades.getRegistration());
            return ResponseEntity.ok(gradesDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<GradesEntity>> findAll(){
        List<GradesEntity> grades = gradesService.findAll();
        if (grades.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(grades);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateById (@PathVariable("id") Long id, @RequestBody GradesDTO dto){
        try {
            Optional<GradesEntity> gradesEntityOptional = gradesService.findById(id);
            if (gradesEntityOptional.isPresent()){
                var grades = gradesEntityOptional.get();
                grades.setGrade(dto.grade());
                grades.setBimester(dto.bimester());
                grades.setRegistration(dto.registration());
                gradesService.updateById(grades);
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
        Optional<GradesEntity> gradesEntityOptional = gradesService.findById(id);{
            if (gradesEntityOptional.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            gradesService.deleteById(gradesEntityOptional.get());
            return ResponseEntity.noContent().build();
        }
    }

}



package com.github.digu_dev.schoolapi.grades;

import com.github.digu_dev.schoolapi.exceptions.DuplicatedRegisterException;
import com.github.digu_dev.schoolapi.exceptions.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/grades")
public class GradesController {

    @Autowired
    GradesService gradesService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody GradesDTO gradesDTO){
        try{
            GradesEntity gradesEntity = gradesDTO.mappedByGradesEntity();
            gradesService.save(gradesEntity);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(gradesEntity.getId()).toUri();

            return ResponseEntity.created(location).build();
        }catch (DuplicatedRegisterException e){
            var dtoError = ResponseError.conflict(e.getMessage());
            return ResponseEntity.status(dtoError.status()).body(dtoError);
        }
    }

}



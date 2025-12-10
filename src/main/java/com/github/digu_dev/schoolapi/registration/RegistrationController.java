package com.github.digu_dev.schoolapi.registration;

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
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<Object> save (@RequestBody RegistrationDTO dto){
        try{
            RegistrationEntity registration = dto.mappedByRegistrationDTO();
            registrationService.save(dto);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{registrationId}")
                    .buildAndExpand(registration.getRegistrationId()).toUri();
            return ResponseEntity.created(location).build();
        }catch (DuplicatedRegisterException e){
            ResponseError conflict = ResponseError.conflict(e.getLocalizedMessage());
            return ResponseEntity.status(conflict.status()).body(conflict);
        }
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<RegistrationDTO> findById (@PathVariable("registrationId") Long registrationId){
        Optional<RegistrationEntity> registrationEntityOptional = registrationService.findById(registrationId);
        if(registrationEntityOptional.isPresent()){
            RegistrationEntity registration = registrationEntityOptional.get();
            RegistrationDTO registrationDTO = new RegistrationDTO(registration.getRegistrationId(),
                    registration.getStudent(),
                    registration.getSubject(),
                    registration.getClassRoom());
            return ResponseEntity.ok(registrationDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<RegistrationEntity>> findAll(){
        List<RegistrationEntity> registrations = registrationService.findAll();
        if (registrations.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registrations);
    }

    @PutMapping("/{registrationId}")
    public ResponseEntity<Object> updateById (@PathVariable ("registrationId") Long registrationId ,
                                              @RequestBody RegistrationDTO dto){
        Optional<RegistrationEntity> registrationEntityOptional = registrationService.findById(registrationId);
        if(registrationEntityOptional.isPresent()){
            var registration = registrationEntityOptional.get();
            registration.setStudent(dto.student());
            registration.setSubject(dto.subject());
            registration.setClassRoom(dto.classRoom());
            registrationService.updateById(registration);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<Void> deleteById (@PathVariable ("registrationId") Long registrationId){
        Optional<RegistrationEntity> registrationEntityOptional = registrationService.findById(registrationId);
        if (registrationEntityOptional.isPresent()){
            registrationService.deleteById(registrationEntityOptional.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

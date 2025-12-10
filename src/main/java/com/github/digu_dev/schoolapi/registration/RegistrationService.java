package com.github.digu_dev.schoolapi.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;

    public RegistrationEntity save (RegistrationDTO dto){
        RegistrationEntity registration = new RegistrationEntity();
        registration.setStudent(dto.student());
        registration.setSubject(dto.subject());
        registration.setClassRoom(dto.classRoom());
        return registrationRepository.save(registration);
    }

    public Optional<RegistrationEntity> findById(Long id){ return registrationRepository.findById(id); }

    public List<RegistrationEntity> findAll(){ return registrationRepository.findAll();}

    public void updateById (RegistrationEntity registration){
        if (registration.getRegistrationId() == null){
            throw new IllegalArgumentException("Registration not Found");
        }
        registrationRepository.save(registration);
    }

    public void deleteById(RegistrationEntity registration){
        if (registration.getRegistrationId() == null){
            throw new IllegalArgumentException("Registration not Found");
        }
        registrationRepository.delete(registration);
    }

}

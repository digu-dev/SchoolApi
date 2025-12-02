package com.github.digu_dev.schoolapi.grades;


import com.github.digu_dev.schoolapi.registration.RegistrationEntity;
import com.github.digu_dev.schoolapi.registration.RegistrationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradesService {

    @Autowired
    GradesRepository gradesRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    public GradesEntity save(GradesDTO dto) {
        RegistrationEntity registration = registrationRepository.findById(dto.mappedByGradesEntity()
                        .getRegistration().getRegistrationId())
                .orElseThrow(() -> new EntityNotFoundException("Registration not found with ID: " + dto.mappedByGradesEntity()
                        .getRegistration().getRegistrationId()));

        GradesEntity gradeEntity = new GradesEntity();
        gradeEntity.setGrade(dto.grade());
        gradeEntity.setBimester(dto.bimester());
        gradeEntity.setRegistration(dto.registration());
        return gradesRepository.save(gradeEntity);
    }
}
// fazer o RegistrationEntity primeiro que ai sim da pra criar uma nota!!!
// na verdade tente fazer metodos post para todas as entidades primeiro e ai sim ve se da certo!!
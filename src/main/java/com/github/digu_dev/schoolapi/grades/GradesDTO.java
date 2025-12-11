package com.github.digu_dev.schoolapi.grades;


import com.github.digu_dev.schoolapi.registration.RegistrationEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GradesDTO(Long id,
                        @NotNull(message = "Required field!")
                        Integer bimester,
                        @NotNull(message = "Required field!")
                        Double grade,
                        @NotBlank(message = "Required field!")
                        RegistrationEntity registration) {

    public GradesEntity mappedByGradesEntity(){
        GradesEntity grades = new GradesEntity();
        grades.setGrade(this.grade);
        grades.setBimester(this.bimester);
        grades.setRegistration(this.registration);
        return grades;
    }


}

package com.github.digu_dev.schoolapi.subject;

import jakarta.validation.constraints.NotBlank;

public record SubjectDTO(Long id,
                         @NotBlank(message = "Required field!")
                         String name) {

    public SubjectEntity mappedBySubjectEntity (){
        SubjectEntity subject = new SubjectEntity();
        subject.setName(this.name);
        return subject;
    }
}



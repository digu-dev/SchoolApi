package com.github.digu_dev.schoolapi.registration;

import com.github.digu_dev.schoolapi.classroom.ClassRoomEntity;
import com.github.digu_dev.schoolapi.student.StudentEntity;
import com.github.digu_dev.schoolapi.subject.SubjectEntity;
import jakarta.validation.constraints.NotBlank;

public record RegistrationDTO(Long registrationId,
                              @NotBlank(message = "Required field")
                              StudentEntity student,
                              @NotBlank(message = "Required field")
                              SubjectEntity subject,
                              @NotBlank(message = "Required field")
                              ClassRoomEntity classRoom) {

    public RegistrationEntity mappedByRegistrationDTO(){
        RegistrationEntity registration = new RegistrationEntity();
        registration.setStudent(this.student);
        registration.setSubject(this.subject);
        registration.setClassRoom(this.classRoom);
        return registration;
    }
}

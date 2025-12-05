package com.github.digu_dev.schoolapi.classroom;

import com.github.digu_dev.schoolapi.enums.Period;
import jakarta.validation.constraints.NotBlank;

public record ClassRoomDTO(Long id,
                           @NotBlank(message = "Required field")
                           Period period) {

    public ClassRoomEntity mappedByClassRoomEntity(){
        ClassRoomEntity classRoom = new ClassRoomEntity();
        classRoom.setPeriod(this.period);
        return classRoom;
    }
}

package digu_dev.com.github.SchoolAPI.dto;

import digu_dev.com.github.SchoolAPI.entity.SchoolClass;

public record SchoolClassDto(Long id, String classCode) {

    public SchoolClassDto(SchoolClass schoolClass) {
        this(schoolClass.getId(), schoolClass.getClassCode());
    }

}

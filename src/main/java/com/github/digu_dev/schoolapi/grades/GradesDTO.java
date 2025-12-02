package com.github.digu_dev.schoolapi.grades;


import jakarta.validation.constraints.NotNull;

public record GradesDTO(Long id,
                        @NotNull(message = "Required field!")
                        Integer bimester,
                        @NotNull(message = "Required field!")
                        Double grade) {

    public GradesEntity mappedByGradesEntity(){
        GradesEntity grades = new GradesEntity();
        grades.setGrade(this.grade);
        grades.setBimester(this.bimester);
        return grades;
    }


}

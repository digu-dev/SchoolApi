package com.github.digu_dev.schoolapi.grades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradesService {

    @Autowired
    GradesRepository gradesRepository;

    public GradesEntity save(GradesEntity grades) {
        return gradesRepository.save(grades);
    }


}

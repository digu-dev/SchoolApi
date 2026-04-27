package digu_dev.com.github.SchoolAPI.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import digu_dev.com.github.SchoolAPI.dto.TeacherDto;
import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.entity.Teacher;
import digu_dev.com.github.SchoolAPI.repository.SubjectRepository;
import digu_dev.com.github.SchoolAPI.repository.TeacherRepository;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@PreAuthorize("hasRole('ADMIN')")
public class TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Transactional(rollbackFor = Exception.class)
    public Teacher create (TeacherDto teacherDto) {
        Teacher teacher = new Teacher();
        teacher.setName(teacherDto.name());
        teacher.setEmail(teacherDto.email());
            for (String subjectName : teacherDto.subject()) {
                Subject subject = subjectRepository.findByName(subjectName)
                    .orElseThrow(() -> new RuntimeException("Subject not found: " + subjectName));
                teacher.getSubjects().add(subject);
            }
        return teacherRepository.save(teacher);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update (Teacher teacher) {
        if (teacher.getId() == null) {
            throw new RuntimeException("Teacher ID is required for update");
        }
        teacherRepository.save(teacher);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete (Teacher teacher) {
        if (teacher.getId() == null) {
            throw new RuntimeException("Teacher ID is required for deletion");
        }
        teacherRepository.delete(teacher); 
        
    }

    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findByIdWithSubjects(Long id) {
        return teacherRepository.findByIdWithSubjects(id);
    }

    public Optional<Teacher> findByIdWithDepartment(Long id) {
        return teacherRepository.findByIdWithDepartment(id);
    }   

    public List<Teacher> findByDepartmentName(String departmentName) {
        return teacherRepository.findByDepartmentName(departmentName);
    }

    

}

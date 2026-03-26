package digu_dev.com.github.SchoolAPI.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import digu_dev.com.github.SchoolAPI.dto.StudentDto;
import digu_dev.com.github.SchoolAPI.entity.Student;
import digu_dev.com.github.SchoolAPI.repository.SchoolClassRepository;
import digu_dev.com.github.SchoolAPI.repository.StudentRepository;

@Service
@Transactional(readOnly = true, rollbackFor=Exception.class)
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SchoolClassRepository schoolClassRepository;

    @Transactional(rollbackFor = Exception.class)
    public Student create (StudentDto studentDto) {
        Student student = new Student();
        student.setName(studentDto.name());
        student.setRegistration(studentDto.registration());
        student.setSchoolClass(schoolClassRepository.findByClassCode(studentDto.schoolClassCode())
                .orElseThrow(() -> new RuntimeException("School class not found")));
        
        return studentRepository.save(student);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Student student) {
        if (student.getId() == null) {
            throw new RuntimeException("Student ID is required for update");
        }

        studentRepository.save(student);
    }   

    @Transactional(rollbackFor = Exception.class)
    public void delete(Student student) {
        if (student.getId() == null) {
            throw new RuntimeException("Student ID is required for update");
        }

        studentRepository.delete(student);
    }   

    public Optional<Student> findByRegistrationWithClass(String registration) {
        return studentRepository.findByRegistrationWithClass(registration);
                
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

}

package digu_dev.com.github.SchoolAPI.tests.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import digu_dev.com.github.SchoolAPI.dto.StudentDto;
import digu_dev.com.github.SchoolAPI.entity.SchoolClass;
import digu_dev.com.github.SchoolAPI.entity.Student;
import digu_dev.com.github.SchoolAPI.repository.SchoolClassRepository;
import digu_dev.com.github.SchoolAPI.repository.StudentRepository;
import digu_dev.com.github.SchoolAPI.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void create_shouldSaveStudent_whenClassExists() {
        StudentDto dto = new StudentDto(null, "Alice", "2024001", "A1");

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(1L);
        schoolClass.setClassCode("A1");

        Student saved = new Student();
        saved.setId(1L);
        saved.setName("Alice");
        saved.setRegistration("2024001");
        saved.setSchoolClass(schoolClass);

        when(schoolClassRepository.findByClassCode("A1")).thenReturn(Optional.of(schoolClass));
        when(studentRepository.save(any(Student.class))).thenReturn(saved);

        Student result = studentService.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(result.getRegistration()).isEqualTo("2024001");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void create_shouldThrowRuntimeException_whenClassNotFound() {
        StudentDto dto = new StudentDto(null, "Bob", "2024002", "Z9");

        when(schoolClassRepository.findByClassCode("Z9")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("School class not found");
    }

    @Test
    void update_shouldSaveStudent_whenIdIsNotNull() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Updated Name");

        studentService.update(student);

        verify(studentRepository).save(student);
    }

    @Test
    void update_shouldThrowRuntimeException_whenIdIsNull() {
        Student student = new Student();
        student.setId(null);

        assertThatThrownBy(() -> studentService.update(student))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Student ID is required for update");
    }

    @Test
    void delete_shouldDeleteStudent_whenIdIsNotNull() {
        Student student = new Student();
        student.setId(1L);

        studentService.delete(student);

        verify(studentRepository).delete(student);
    }

    @Test
    void delete_shouldThrowRuntimeException_whenIdIsNull() {
        Student student = new Student();
        student.setId(null);

        assertThatThrownBy(() -> studentService.delete(student))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void findById_shouldReturnStudent_whenExists() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Alice");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldReturnListOfStudents() {
        Student s1 = new Student();
        s1.setId(1L);
        s1.setName("Alice");

        Student s2 = new Student();
        s2.setId(2L);
        s2.setName("Bob");

        when(studentRepository.findAll()).thenReturn(List.of(s1, s2));

        List<Student> result = studentService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Student::getName).containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    void findByRegistrationWithClass_shouldReturnStudent_whenExists() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(1L);
        schoolClass.setClassCode("A1");

        Student student = new Student();
        student.setId(1L);
        student.setRegistration("2024001");
        student.setSchoolClass(schoolClass);

        when(studentRepository.findByRegistrationWithClass("2024001")).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findByRegistrationWithClass("2024001");

        assertThat(result).isPresent();
        assertThat(result.get().getRegistration()).isEqualTo("2024001");
    }

    @Test
    void findByRegistrationWithClass_shouldReturnEmpty_whenNotExists() {
        when(studentRepository.findByRegistrationWithClass("UNKNOWN")).thenReturn(Optional.empty());

        Optional<Student> result = studentService.findByRegistrationWithClass("UNKNOWN");

        assertThat(result).isEmpty();
    }
}

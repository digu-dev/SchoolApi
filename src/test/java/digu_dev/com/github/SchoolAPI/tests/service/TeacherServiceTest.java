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
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import digu_dev.com.github.SchoolAPI.dto.TeacherDto;
import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.entity.Teacher;
import digu_dev.com.github.SchoolAPI.repository.SubjectRepository;
import digu_dev.com.github.SchoolAPI.repository.TeacherRepository;
import digu_dev.com.github.SchoolAPI.service.TeacherService;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void create_shouldSaveTeacherWithSubjects_whenSubjectsExist() {
        TeacherDto dto = new TeacherDto(null, "Alice", "alice@school.com", List.of("Math", "Physics"));

        Subject math = new Subject();
        math.setId(1L);
        math.setName("Math");

        Subject physics = new Subject();
        physics.setId(2L);
        physics.setName("Physics");

        Teacher saved = new Teacher();
        saved.setId(1L);
        saved.setName("Alice");
        saved.setEmail("alice@school.com");

        when(subjectRepository.findByName("Math")).thenReturn(Optional.of(math));
        when(subjectRepository.findByName("Physics")).thenReturn(Optional.of(physics));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(saved);

        Teacher result = teacherService.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Alice");
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void create_shouldThrowRuntimeException_whenSubjectNotFound() {
        TeacherDto dto = new TeacherDto(null, "Bob", "bob@school.com", List.of("NonExistentSubject"));

        when(subjectRepository.findByName("NonExistentSubject")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Subject not found: NonExistentSubject");
    }

    @Test
    void update_shouldSaveTeacher_whenIdIsNotNull() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Updated Name");

        teacherService.update(teacher);

        verify(teacherRepository).save(teacher);
    }

    @Test
    void update_shouldThrowRuntimeException_whenIdIsNull() {
        Teacher teacher = new Teacher();
        teacher.setId(null);

        assertThatThrownBy(() -> teacherService.update(teacher))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Teacher ID is required for update");
    }

    @Test
    void delete_shouldDeleteTeacher_whenIdIsNotNull() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        teacherService.delete(teacher);

        verify(teacherRepository).delete(teacher);
    }

    @Test
    void delete_shouldThrowRuntimeException_whenIdIsNull() {
        Teacher teacher = new Teacher();
        teacher.setId(null);

        assertThatThrownBy(() -> teacherService.delete(teacher))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Teacher ID is required for deletion");
    }

    @Test
    void findById_shouldReturnTeacher_whenExists() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("John");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("John");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Teacher> result = teacherService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldReturnListOfTeachers() {
        Teacher t1 = new Teacher();
        t1.setId(1L);
        t1.setName("Alice");

        Teacher t2 = new Teacher();
        t2.setId(2L);
        t2.setName("Bob");

        when(teacherRepository.findAll()).thenReturn(List.of(t1, t2));

        List<Teacher> result = teacherService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Teacher::getName).containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    void findByIdWithSubjects_shouldReturnTeacher_whenExists() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(teacherRepository.findByIdWithSubjects(1L)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.findByIdWithSubjects(1L);

        assertThat(result).isPresent();
    }

    @Test
    void findByIdWithDepartment_shouldReturnTeacher_whenExists() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(teacherRepository.findByIdWithDepartment(1L)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.findByIdWithDepartment(1L);

        assertThat(result).isPresent();
    }

    @Test
    void findByDepartmentName_shouldReturnList() {
        Teacher t1 = new Teacher();
        t1.setId(1L);

        when(teacherRepository.findByDepartmentName("Math")).thenReturn(List.of(t1));

        List<Teacher> result = teacherService.findByDepartmentName("Math");

        assertThat(result).hasSize(1);
    }

    @Test
    void findByDepartmentName_shouldReturnEmptyList_whenNoneFound() {
        when(teacherRepository.findByDepartmentName("Unknown")).thenReturn(List.of());

        List<Teacher> result = teacherService.findByDepartmentName("Unknown");

        assertThat(result).isEmpty();
    }
}

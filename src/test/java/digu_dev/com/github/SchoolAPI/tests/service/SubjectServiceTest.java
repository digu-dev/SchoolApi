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

import digu_dev.com.github.SchoolAPI.dto.SubjectDto;
import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.repository.SubjectRepository;
import digu_dev.com.github.SchoolAPI.service.SubjectService;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void create_shouldSaveAndReturnSubject() {
        SubjectDto dto = new SubjectDto(null, "Mathematics");

        Subject saved = new Subject();
        saved.setId(1L);
        saved.setName("Mathematics");

        when(subjectRepository.save(any(Subject.class))).thenReturn(saved);

        Subject result = subjectService.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Mathematics");
        verify(subjectRepository).save(any(Subject.class));
    }

    @Test
    void update_shouldSaveSubject_whenIdIsNotNull() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Updated Subject");

        subjectService.update(subject);

        verify(subjectRepository).save(subject);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenIdIsNull() {
        Subject subject = new Subject();
        subject.setId(null);

        assertThatThrownBy(() -> subjectService.update(subject))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Subject ID must not be null for update");
    }

    @Test
    void delete_shouldDeleteSubject_whenIdIsNotNull() {
        Subject subject = new Subject();
        subject.setId(1L);

        subjectService.delete(subject);

        verify(subjectRepository).delete(subject);
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_whenIdIsNull() {
        Subject subject = new Subject();
        subject.setId(null);

        assertThatThrownBy(() -> subjectService.delete(subject))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Subject ID must not be null");
    }

    @Test
    void findById_shouldReturnSubject_whenExists() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Chemistry");

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        Optional<Subject> result = subjectService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Chemistry");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(subjectRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Subject> result = subjectService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldReturnListOfSubjects() {
        Subject s1 = new Subject();
        s1.setId(1L);
        s1.setName("Math");

        Subject s2 = new Subject();
        s2.setId(2L);
        s2.setName("Biology");

        when(subjectRepository.findAll()).thenReturn(List.of(s1, s2));

        List<Subject> result = subjectService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Subject::getName).containsExactlyInAnyOrder("Math", "Biology");
    }

    @Test
    void findByNameWithTeachers_shouldReturnSubject_whenExists() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        when(subjectRepository.findByNameWithTeachers("Math")).thenReturn(Optional.of(subject));

        Optional<Subject> result = subjectService.findByNameWithTeachers("Math");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Math");
    }

    @Test
    void findByNameWithTeachers_shouldReturnEmpty_whenNotExists() {
        when(subjectRepository.findByNameWithTeachers("Unknown")).thenReturn(Optional.empty());

        Optional<Subject> result = subjectService.findByNameWithTeachers("Unknown");

        assertThat(result).isEmpty();
    }

    @Test
    void findByName_shouldReturnSubject_whenExists() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("History");

        when(subjectRepository.findByName("History")).thenReturn(Optional.of(subject));

        Optional<Subject> result = subjectService.findByName("History");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("History");
    }

    @Test
    void findByName_shouldReturnEmpty_whenNotExists() {
        when(subjectRepository.findByName("Unknown")).thenReturn(Optional.empty());

        Optional<Subject> result = subjectService.findByName("Unknown");

        assertThat(result).isEmpty();
    }
}

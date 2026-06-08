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

import digu_dev.com.github.SchoolAPI.dto.GPADto;
import digu_dev.com.github.SchoolAPI.entity.GPA;
import digu_dev.com.github.SchoolAPI.entity.StatusEnum;
import digu_dev.com.github.SchoolAPI.entity.Student;
import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.repository.GPARepository;
import digu_dev.com.github.SchoolAPI.service.GPAService;

@ExtendWith(MockitoExtension.class)
class GPAServiceTest {

    @Mock
    private GPARepository gpaRepository;

    @InjectMocks
    private GPAService gpaService;

    @Test
    void calculateFinalGrade_shouldReturnSumOfAllGrades() {
        Double result = gpaService.calculateFinalGrade(25.0, 25.0, 35.0);

        assertThat(result).isEqualTo(85.0);
    }

    @Test
    void calculateFinalGrade_shouldReturnZero_whenAllGradesAreZero() {
        Double result = gpaService.calculateFinalGrade(0.0, 0.0, 0.0);

        assertThat(result).isEqualTo(0.0);
    }

    @Test
    void calculateFinalGrade_shouldThrowIllegalArgumentException_whenG1IsNull() {
        assertThatThrownBy(() -> gpaService.calculateFinalGrade(null, 25.0, 35.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("All grade components must be provided");
    }

    @Test
    void calculateFinalGrade_shouldThrowIllegalArgumentException_whenG2IsNull() {
        assertThatThrownBy(() -> gpaService.calculateFinalGrade(25.0, null, 35.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("All grade components must be provided");
    }

    @Test
    void calculateFinalGrade_shouldThrowIllegalArgumentException_whenG3IsNull() {
        assertThatThrownBy(() -> gpaService.calculateFinalGrade(25.0, 25.0, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("All grade components must be provided");
    }

    @Test
    void createGPA_shouldCalculateFinalGradeAndSave() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Alice");

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        GPADto dto = new GPADto(null, 25.0, 25.0, 35.0, 85.0, subject, student, StatusEnum.APPROVED);

        GPA saved = new GPA();
        saved.setId(1L);
        saved.setGrade1(25.0);
        saved.setGrade2(25.0);
        saved.setGrade3(35.0);
        saved.setFinalGrade(85.0);
        saved.setStudent(student);
        saved.setSubject(subject);
        saved.setStatus(StatusEnum.APPROVED);

        when(gpaRepository.save(any(GPA.class))).thenReturn(saved);

        GPA result = gpaService.createGPA(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getGrade1()).isEqualTo(25.0);
        assertThat(result.getGrade2()).isEqualTo(25.0);
        assertThat(result.getGrade3()).isEqualTo(35.0);
        assertThat(result.getFinalGrade()).isEqualTo(85.0);
        assertThat(result.getStatus()).isEqualTo(StatusEnum.APPROVED);
        verify(gpaRepository).save(any(GPA.class));
    }

    @Test
    void update_shouldSaveGPA_whenIdIsNotNull() {
        GPA gpa = new GPA();
        gpa.setId(1L);
        gpa.setGrade1(20.0);

        gpaService.update(gpa);

        verify(gpaRepository).save(gpa);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenIdIsNull() {
        GPA gpa = new GPA();
        gpa.setId(null);

        assertThatThrownBy(() -> gpaService.update(gpa))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("GPA ID cannot be null for update");
    }

    @Test
    void delete_shouldDeleteById_whenIdIsNotNull() {
        GPA gpa = new GPA();
        gpa.setId(1L);

        gpaService.delete(gpa);

        verify(gpaRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_whenIdIsNull() {
        GPA gpa = new GPA();
        gpa.setId(null);

        assertThatThrownBy(() -> gpaService.delete(gpa))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("GPA ID cannot be null for deletion");
    }

    @Test
    void findById_shouldReturnGPA_whenExists() {
        GPA gpa = new GPA();
        gpa.setId(1L);
        gpa.setFinalGrade(90.0);

        when(gpaRepository.findById(1L)).thenReturn(Optional.of(gpa));

        Optional<GPA> result = gpaService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getFinalGrade()).isEqualTo(90.0);
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(gpaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<GPA> result = gpaService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldReturnListOfGPAs() {
        GPA gpa1 = new GPA();
        gpa1.setId(1L);
        gpa1.setFinalGrade(85.0);

        GPA gpa2 = new GPA();
        gpa2.setId(2L);
        gpa2.setFinalGrade(90.0);

        when(gpaRepository.findAll()).thenReturn(List.of(gpa1, gpa2));

        List<GPA> result = gpaService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(GPA::getFinalGrade).containsExactlyInAnyOrder(85.0, 90.0);
    }

    @Test
    void findByStudentWithDetails_shouldReturnListForStudent() {
        GPA gpa = new GPA();
        gpa.setId(1L);
        gpa.setFinalGrade(75.0);

        when(gpaRepository.findByStudentWithDetails(1L)).thenReturn(List.of(gpa));

        List<GPA> result = gpaService.findByStudentWithDetails(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFinalGrade()).isEqualTo(75.0);
    }

    @Test
    void findByStudentWithDetails_shouldReturnEmptyList_whenNoGPAs() {
        when(gpaRepository.findByStudentWithDetails(99L)).thenReturn(List.of());

        List<GPA> result = gpaService.findByStudentWithDetails(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void findFinalGradeByStudentAndSubject_shouldReturnGrade() {
        when(gpaRepository.findFinalGradeByStudentAndSubject(1L, 1L)).thenReturn(88.0);

        Double result = gpaService.findFinalGradeByStudentAndSubject(1L, 1L);

        assertThat(result).isEqualTo(88.0);
    }

    @Test
    void findFinalGradeByStudentAndSubject_shouldReturnNull_whenNotFound() {
        when(gpaRepository.findFinalGradeByStudentAndSubject(1L, 99L)).thenReturn(null);

        Double result = gpaService.findFinalGradeByStudentAndSubject(1L, 99L);

        assertThat(result).isNull();
    }

    @Test
    void findAllFinalGradesByStudent_shouldReturnListOfGrades() {
        when(gpaRepository.findAllFinalGradesByStudent(1L)).thenReturn(List.of(85.0, 90.0, 78.0));

        List<Double> result = gpaService.findAllFinalGradesByStudent(1L);

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrder(85.0, 90.0, 78.0);
    }

    @Test
    void findAllFinalGradesByStudent_shouldReturnEmptyList_whenNoGrades() {
        when(gpaRepository.findAllFinalGradesByStudent(99L)).thenReturn(List.of());

        List<Double> result = gpaService.findAllFinalGradesByStudent(99L);

        assertThat(result).isEmpty();
    }
}

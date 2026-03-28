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

import digu_dev.com.github.SchoolAPI.dto.SchoolClassDto;
import digu_dev.com.github.SchoolAPI.entity.SchoolClass;
import digu_dev.com.github.SchoolAPI.repository.SchoolClassRepository;
import digu_dev.com.github.SchoolAPI.service.SchoolClassService;

@ExtendWith(MockitoExtension.class)
class SchoolClassServiceTest {

    @Mock
    private SchoolClassRepository schoolClassRepository;

    @InjectMocks
    private SchoolClassService schoolClassService;

    @Test
    void create_shouldSaveAndReturnSchoolClass() {
        SchoolClassDto dto = new SchoolClassDto(null, "A1");

        SchoolClass saved = new SchoolClass();
        saved.setId(1L);
        saved.setClassCode("A1");

        when(schoolClassRepository.save(any(SchoolClass.class))).thenReturn(saved);

        SchoolClass result = schoolClassService.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getClassCode()).isEqualTo("A1");
        verify(schoolClassRepository).save(any(SchoolClass.class));
    }

    @Test
    void update_shouldSaveSchoolClass_whenIdIsNotNull() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(1L);
        schoolClass.setClassCode("B2");

        schoolClassService.update(schoolClass);

        verify(schoolClassRepository).save(schoolClass);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenIdIsNull() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(null);

        assertThatThrownBy(() -> schoolClassService.update(schoolClass))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("SchoolClass ID must not be null for update");
    }

    @Test
    void delete_shouldDeleteSchoolClass_whenIdIsNotNull() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(1L);

        schoolClassService.delete(schoolClass);

        verify(schoolClassRepository).delete(schoolClass);
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_whenIdIsNull() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(null);

        assertThatThrownBy(() -> schoolClassService.delete(schoolClass))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("SchoolClass ID must not be null for deletion");
    }

    @Test
    void findById_shouldReturnSchoolClass_whenExists() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(1L);
        schoolClass.setClassCode("C3");

        when(schoolClassRepository.findById(1L)).thenReturn(Optional.of(schoolClass));

        Optional<SchoolClass> result = schoolClassService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getClassCode()).isEqualTo("C3");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(schoolClassRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<SchoolClass> result = schoolClassService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldReturnListOfSchoolClasses() {
        SchoolClass sc1 = new SchoolClass();
        sc1.setId(1L);
        sc1.setClassCode("A1");

        SchoolClass sc2 = new SchoolClass();
        sc2.setId(2L);
        sc2.setClassCode("B2");

        when(schoolClassRepository.findAll()).thenReturn(List.of(sc1, sc2));

        List<SchoolClass> result = schoolClassService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(SchoolClass::getClassCode).containsExactlyInAnyOrder("A1", "B2");
    }

    @Test
    void findByClassCode_shouldReturnSchoolClass_whenExists() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(1L);
        schoolClass.setClassCode("D4");

        when(schoolClassRepository.findByClassCode("D4")).thenReturn(Optional.of(schoolClass));

        Optional<SchoolClass> result = schoolClassService.findByClassCode("D4");

        assertThat(result).isPresent();
        assertThat(result.get().getClassCode()).isEqualTo("D4");
    }

    @Test
    void findByClassCode_shouldReturnEmpty_whenNotExists() {
        when(schoolClassRepository.findByClassCode("ZZ")).thenReturn(Optional.empty());

        Optional<SchoolClass> result = schoolClassService.findByClassCode("ZZ");

        assertThat(result).isEmpty();
    }

    @Test
    void findByCodeWithStudents_shouldReturnSchoolClass_whenExists() {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(1L);
        schoolClass.setClassCode("E5");

        when(schoolClassRepository.findByCodeWithStudents("E5")).thenReturn(Optional.of(schoolClass));

        Optional<SchoolClass> result = schoolClassService.findByCodeWithStudents("E5");

        assertThat(result).isPresent();
        assertThat(result.get().getClassCode()).isEqualTo("E5");
    }

    @Test
    void findByCodeWithStudents_shouldReturnEmpty_whenNotExists() {
        when(schoolClassRepository.findByCodeWithStudents("ZZ")).thenReturn(Optional.empty());

        Optional<SchoolClass> result = schoolClassService.findByCodeWithStudents("ZZ");

        assertThat(result).isEmpty();
    }
}

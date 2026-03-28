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

import digu_dev.com.github.SchoolAPI.dto.DepartmentDto;
import digu_dev.com.github.SchoolAPI.entity.Department;
import digu_dev.com.github.SchoolAPI.repository.DepartmentRepository;
import digu_dev.com.github.SchoolAPI.service.DepartmentService;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void create_shouldSaveAndReturnDepartment() {
        DepartmentDto dto = new DepartmentDto(null, "Mathematics");

        Department saved = new Department();
        saved.setId(1L);
        saved.setName("Mathematics");

        when(departmentRepository.save(any(Department.class))).thenReturn(saved);

        Department result = departmentService.create(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Mathematics");
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void update_shouldSaveDepartment_whenIdIsNotNull() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Updated Department");

        departmentService.update(department);

        verify(departmentRepository).save(department);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenIdIsNull() {
        Department department = new Department();
        department.setId(null);

        assertThatThrownBy(() -> departmentService.update(department))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Department ID cannot be null for update");
    }

    @Test
    void delete_shouldDeleteById_whenIdIsNotNull() {
        Department department = new Department();
        department.setId(1L);

        departmentService.delete(department);

        verify(departmentRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_whenIdIsNull() {
        Department department = new Department();
        department.setId(null);

        assertThatThrownBy(() -> departmentService.delete(department))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Department ID cannot be null for deletion");
    }

    @Test
    void findById_shouldReturnDepartment_whenExists() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Science");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Optional<Department> result = departmentService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Science");
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Department> result = departmentService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldReturnListOfDepartments() {
        Department d1 = new Department();
        d1.setId(1L);
        d1.setName("Math");

        Department d2 = new Department();
        d2.setId(2L);
        d2.setName("Science");

        when(departmentRepository.findAll()).thenReturn(List.of(d1, d2));

        List<Department> result = departmentService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Department::getName).containsExactlyInAnyOrder("Math", "Science");
    }

    @Test
    void findByName_shouldReturnDepartment_whenExists() {
        Department department = new Department();
        department.setId(1L);
        department.setName("History");

        when(departmentRepository.findByName("History")).thenReturn(Optional.of(department));

        Optional<Department> result = departmentService.findByName("History");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("History");
    }

    @Test
    void findByName_shouldReturnEmpty_whenNotExists() {
        when(departmentRepository.findByName("Unknown")).thenReturn(Optional.empty());

        Optional<Department> result = departmentService.findByName("Unknown");

        assertThat(result).isEmpty();
    }

    @Test
    void findByIdWithTeachers_shouldReturnDepartmentWithTeachers_whenExists() {
        Department department = new Department();
        department.setId(1L);
        department.setName("Physics");

        when(departmentRepository.findByIdWithTeachers(1L)).thenReturn(Optional.of(department));

        Optional<Department> result = departmentService.findByIdWithTeachers(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void findByIdWithTeachers_shouldReturnEmpty_whenNotExists() {
        when(departmentRepository.findByIdWithTeachers(99L)).thenReturn(Optional.empty());

        Optional<Department> result = departmentService.findByIdWithTeachers(99L);

        assertThat(result).isEmpty();
    }
}

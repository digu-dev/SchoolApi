package digu_dev.com.github.SchoolAPI.tests.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import digu_dev.com.github.SchoolAPI.controller.DepartmentController;
import digu_dev.com.github.SchoolAPI.dto.DepartmentDto;
import digu_dev.com.github.SchoolAPI.entity.Department;
import digu_dev.com.github.SchoolAPI.service.DepartmentService;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createDepartment_shouldReturn201_whenValidDto() throws Exception {
        DepartmentDto dto = new DepartmentDto(null, "Mathematics");

        Department saved = new Department();
        saved.setId(1L);
        saved.setName("Mathematics");

        when(departmentService.create(any(DepartmentDto.class))).thenReturn(saved);

        mockMvc.perform(post("/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateDepartment_shouldReturn204_whenFound() throws Exception {
        DepartmentDto dto = new DepartmentDto(null, "Updated Department");

        Department existing = new Department();
        existing.setId(1L);
        existing.setName("Old Department");

        when(departmentService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(departmentService).update(any(Department.class));

        mockMvc.perform(put("/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateDepartment_shouldReturn404_whenNotFound() throws Exception {
        DepartmentDto dto = new DepartmentDto(null, "Updated Department");

        when(departmentService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/departments/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDepartment_shouldReturn204_whenFound() throws Exception {
        Department existing = new Department();
        existing.setId(1L);
        existing.setName("Math");

        when(departmentService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(departmentService).delete(any(Department.class));

        mockMvc.perform(delete("/departments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteDepartment_shouldReturn404_whenNotFound() throws Exception {
        when(departmentService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/departments/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findDepartmentById_shouldReturn200WithDto_whenFound() throws Exception {
        Department department = new Department();
        department.setId(1L);
        department.setName("Science");

        when(departmentService.findById(1L)).thenReturn(Optional.of(department));

        mockMvc.perform(get("/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Science"));
    }

    @Test
    void findDepartmentById_shouldReturn404_whenNotFound() throws Exception {
        when(departmentService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/departments/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllDepartments_shouldReturn200WithList_whenNotEmpty() throws Exception {
        Department d1 = new Department();
        d1.setId(1L);
        d1.setName("Math");
        d1.setTeachers(null);

        Department d2 = new Department();
        d2.setId(2L);
        d2.setName("Science");
        d2.setTeachers(null);

        when(departmentService.findAll()).thenReturn(List.of(d1, d2));

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findAllDepartments_shouldReturn404_whenEmpty() throws Exception {
        when(departmentService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/departments"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByName_shouldReturn200WithDto_whenFound() throws Exception {
        Department department = new Department();
        department.setId(1L);
        department.setName("History");

        when(departmentService.findByName("History")).thenReturn(Optional.of(department));

        mockMvc.perform(get("/departments/name").param("name", "History"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("History"));
    }

    @Test
    void findByName_shouldReturn404_whenNotFound() throws Exception {
        when(departmentService.findByName("Unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/departments/name").param("name", "Unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdWithTeachers_shouldReturn200_whenFound() throws Exception {
        Department department = new Department();
        department.setId(1L);
        department.setName("Physics");
        department.setTeachers(null);

        when(departmentService.findByIdWithTeachers(1L)).thenReturn(Optional.of(department));

        mockMvc.perform(get("/departments/1/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Physics"));
    }

    @Test
    void findByIdWithTeachers_shouldReturn404_whenNotFound() throws Exception {
        when(departmentService.findByIdWithTeachers(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/departments/99/teachers"))
                .andExpect(status().isNotFound());
    }
}

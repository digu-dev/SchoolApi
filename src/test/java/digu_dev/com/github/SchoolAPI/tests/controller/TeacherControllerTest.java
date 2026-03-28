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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import digu_dev.com.github.SchoolAPI.controller.TeacherController;
import digu_dev.com.github.SchoolAPI.dto.TeacherDto;
import digu_dev.com.github.SchoolAPI.entity.Department;
import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.entity.Teacher;
import digu_dev.com.github.SchoolAPI.service.TeacherService;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeacherService teacherService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Teacher buildTeacher(Long id, String name, String email) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setName(name);
        teacher.setEmail(email);
        teacher.setDepartment(null);
        teacher.setSubjects(new HashSet<>());
        return teacher;
    }

    @Test
    void createTeacher_shouldReturn201_whenValidDto() throws Exception {
        TeacherDto dto = new TeacherDto(null, "Alice", "alice@school.com", List.of("Math"));

        Teacher saved = buildTeacher(1L, "Alice", "alice@school.com");

        when(teacherService.create(any(TeacherDto.class))).thenReturn(saved);

        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateTeacher_shouldReturn200_whenFound() throws Exception {
        Teacher existing = buildTeacher(1L, "Old Name", "old@school.com");
        Teacher updated = buildTeacher(1L, "New Name", "new@school.com");

        when(teacherService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(teacherService).update(any(Teacher.class));

        mockMvc.perform(put("/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());
    }

    @Test
    void updateTeacher_shouldReturn404_whenNotFound() throws Exception {
        Teacher teacher = buildTeacher(null, "Name", "email@school.com");

        when(teacherService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/teachers/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTeacher_shouldReturn200_whenFound() throws Exception {
        Teacher existing = buildTeacher(1L, "Alice", "alice@school.com");

        when(teacherService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(teacherService).delete(any(Teacher.class));

        mockMvc.perform(delete("/teachers/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTeacher_shouldReturn404_whenNotFound() throws Exception {
        when(teacherService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/teachers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTeacherById_shouldReturn200WithDto_whenFound() throws Exception {
        Teacher teacher = buildTeacher(1L, "Alice", "alice@school.com");

        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@school.com"));
    }

    @Test
    void getTeacherById_shouldReturn404_whenNotFound() throws Exception {
        when(teacherService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/teachers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTeachers_shouldReturn200WithList_whenNotEmpty() throws Exception {
        Teacher t1 = buildTeacher(1L, "Alice", "alice@school.com");
        Teacher t2 = buildTeacher(2L, "Bob", "bob@school.com");

        when(teacherService.findAll()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllTeachers_shouldReturn204_whenEmpty() throws Exception {
        when(teacherService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTeacherSubjects_shouldReturn200_whenSubjectsExist() throws Exception {
        Subject math = new Subject();
        math.setId(1L);
        math.setName("Math");

        Set<Subject> subjects = new HashSet<>();
        subjects.add(math);

        Teacher teacher = buildTeacher(1L, "Alice", "alice@school.com");
        teacher.setSubjects(subjects);

        when(teacherService.findByIdWithSubjects(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teachers/1/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]").value("Math"));
    }

    @Test
    void getTeacherSubjects_shouldReturn204_whenSubjectsEmpty() throws Exception {
        Teacher teacher = buildTeacher(1L, "Alice", "alice@school.com");

        when(teacherService.findByIdWithSubjects(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teachers/1/subjects"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTeacherSubjects_shouldReturn404_whenTeacherNotFound() throws Exception {
        when(teacherService.findByIdWithSubjects(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/teachers/99/subjects"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTeacherDepartment_shouldReturn200WithName_whenDepartmentExists() throws Exception {
        Department dept = new Department();
        dept.setId(1L);
        dept.setName("Science");
        dept.setTeachers(null);

        Teacher teacher = buildTeacher(1L, "Alice", "alice@school.com");
        teacher.setDepartment(dept);

        when(teacherService.findByIdWithDepartment(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teachers/1/department"))
                .andExpect(status().isOk());
    }

    @Test
    void getTeacherDepartment_shouldReturn204_whenDepartmentIsNull() throws Exception {
        Teacher teacher = buildTeacher(1L, "Alice", "alice@school.com");

        when(teacherService.findByIdWithDepartment(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teachers/1/department"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTeacherDepartment_shouldReturn404_whenTeacherNotFound() throws Exception {
        when(teacherService.findByIdWithDepartment(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/teachers/99/department"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTeachersByDepartmentName_shouldReturn200_whenFound() throws Exception {
        Teacher t1 = buildTeacher(1L, "Alice", "alice@school.com");

        when(teacherService.findByDepartmentName("Math")).thenReturn(List.of(t1));

        mockMvc.perform(get("/teachers/department/Math"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getTeachersByDepartmentName_shouldReturn204_whenEmpty() throws Exception {
        when(teacherService.findByDepartmentName("Unknown")).thenReturn(List.of());

        mockMvc.perform(get("/teachers/department/Unknown"))
                .andExpect(status().isNoContent());
    }
}

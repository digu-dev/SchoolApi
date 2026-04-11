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
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import digu_dev.com.github.SchoolAPI.controller.StudentController;
import digu_dev.com.github.SchoolAPI.dto.StudentDto;
import digu_dev.com.github.SchoolAPI.entity.SchoolClass;
import digu_dev.com.github.SchoolAPI.entity.Student;
import digu_dev.com.github.SchoolAPI.service.StudentService;

@DataJpaTest
@ActiveProfiles("test")
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private SchoolClass buildSchoolClass(Long id, String classCode) {
        SchoolClass sc = new SchoolClass();
        sc.setId(id);
        sc.setClassCode(classCode);
        sc.setStudents(null);
        return sc;
    }

    private Student buildStudent(Long id, String name, String registration, SchoolClass schoolClass) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setRegistration(registration);
        student.setSchoolClass(schoolClass);
        student.setGrades(null);
        return student;
    }

    @Test
    void createStudent_shouldReturn201_whenValidDto() throws Exception {
        StudentDto dto = new StudentDto(null, "Alice", "2024001", "A1");

        SchoolClass sc = buildSchoolClass(1L, "A1");
        Student saved = buildStudent(1L, "Alice", "2024001", sc);

        when(studentService.create(any(StudentDto.class))).thenReturn(saved);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateStudent_shouldReturn200_whenFound() throws Exception {
        SchoolClass sc = buildSchoolClass(1L, "A1");
        Student existing = buildStudent(1L, "Old Name", "2024001", sc);
        Student updated = buildStudent(1L, "New Name", "2024001", sc);

        when(studentService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(studentService).update(any(Student.class));

        mockMvc.perform(put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());
    }

    @Test
    void updateStudent_shouldReturn404_whenNotFound() throws Exception {
        SchoolClass sc = buildSchoolClass(1L, "A1");
        Student student = buildStudent(null, "Alice", "2024001", sc);

        when(studentService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/students/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudent_shouldReturn200_whenFound() throws Exception {
        SchoolClass sc = buildSchoolClass(1L, "A1");
        Student existing = buildStudent(1L, "Alice", "2024001", sc);

        when(studentService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(studentService).delete(any(Student.class));

        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteStudent_shouldReturn404_whenNotFound() throws Exception {
        when(studentService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/students/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentById_shouldReturn200WithDto_whenFound() throws Exception {
        SchoolClass sc = buildSchoolClass(1L, "A1");
        Student student = buildStudent(1L, "Alice", "2024001", sc);

        when(studentService.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.registration").value("2024001"))
                .andExpect(jsonPath("$.schoolClassCode").value("A1"));
    }

    @Test
    void getStudentById_shouldReturn404_whenNotFound() throws Exception {
        when(studentService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllStudents_shouldReturn200WithList_whenNotEmpty() throws Exception {
        SchoolClass sc = buildSchoolClass(1L, "A1");
        Student s1 = buildStudent(1L, "Alice", "2024001", sc);
        Student s2 = buildStudent(2L, "Bob", "2024002", sc);

        when(studentService.findAll()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllStudents_shouldReturn204_whenEmpty() throws Exception {
        when(studentService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/students"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getStudentByRegistration_shouldReturn200WithDto_whenFound() throws Exception {
        SchoolClass sc = buildSchoolClass(1L, "A1");
        Student student = buildStudent(1L, "Alice", "2024001", sc);

        when(studentService.findByRegistrationWithClass("2024001")).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/registration/2024001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registration").value("2024001"))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getStudentByRegistration_shouldReturn404_whenNotFound() throws Exception {
        when(studentService.findByRegistrationWithClass("UNKNOWN")).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/registration/UNKNOWN"))
                .andExpect(status().isNotFound());
    }
}

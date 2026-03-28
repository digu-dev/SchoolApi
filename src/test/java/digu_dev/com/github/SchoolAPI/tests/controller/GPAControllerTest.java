package digu_dev.com.github.SchoolAPI.tests.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
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

import digu_dev.com.github.SchoolAPI.controller.GPAController;
import digu_dev.com.github.SchoolAPI.entity.GPA;
import digu_dev.com.github.SchoolAPI.entity.StatusEnum;
import digu_dev.com.github.SchoolAPI.entity.Student;
import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.service.GPAService;

@WebMvcTest(GPAController.class)
class GPAControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GPAService gpaService;

    private GPA buildGPA(Long id, Double g1, Double g2, Double g3, Double finalGrade) {
        GPA gpa = new GPA();
        gpa.setId(id);
        gpa.setGrade1(g1);
        gpa.setGrade2(g2);
        gpa.setGrade3(g3);
        gpa.setFinalGrade(finalGrade);
        gpa.setStatus(StatusEnum.APROVED);
        gpa.setStudent(null);
        gpa.setSubject(null);
        return gpa;
    }

    @Test
    void createGPA_shouldReturn201_whenValidRequest() throws Exception {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Student student = new Student();
        student.setId(1L);
        student.setName("Alice");

        GPA saved = buildGPA(1L, 25.0, 25.0, 35.0, 85.0);

        when(gpaService.createGPA(any())).thenReturn(saved);

        String json = """
                {
                  "g1": 25.0,
                  "g2": 25.0,
                  "g3": 35.0,
                  "finalGrade": 85.0,
                  "subject": {"id": 1, "name": "Math"},
                  "student": {"id": 1, "name": "Alice"},
                  "status": "APROVED"
                }
                """;

        mockMvc.perform(post("/gpa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void updateGPA_shouldReturn204_whenFound() throws Exception {
        GPA existing = buildGPA(1L, 20.0, 20.0, 30.0, 70.0);

        when(gpaService.findById(1L)).thenReturn(Optional.of(existing));
        when(gpaService.calculateFinalGrade(anyDouble(), anyDouble(), anyDouble())).thenReturn(85.0);
        doNothing().when(gpaService).update(any(GPA.class));

        String json = """
                {
                  "g1": 25.0,
                  "g2": 25.0,
                  "g3": 35.0,
                  "finalGrade": 85.0,
                  "subject": {"id": 1, "name": "Math"},
                  "student": {"id": 1, "name": "Alice"},
                  "status": "APROVED"
                }
                """;

        mockMvc.perform(put("/gpa/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateGPA_shouldReturn404_whenNotFound() throws Exception {
        when(gpaService.findById(99L)).thenReturn(Optional.empty());

        String json = """
                {
                  "g1": 25.0,
                  "g2": 25.0,
                  "g3": 35.0,
                  "finalGrade": 85.0,
                  "subject": {"id": 1, "name": "Math"},
                  "student": {"id": 1, "name": "Alice"},
                  "status": "APROVED"
                }
                """;

        mockMvc.perform(put("/gpa/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGPA_shouldReturn204_whenFound() throws Exception {
        GPA existing = buildGPA(1L, 25.0, 25.0, 35.0, 85.0);

        when(gpaService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(gpaService).delete(any(GPA.class));

        mockMvc.perform(delete("/gpa/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteGPA_shouldReturn404_whenNotFound() throws Exception {
        when(gpaService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/gpa/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findGPAById_shouldReturn200WithDto_whenFound() throws Exception {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Student student = new Student();
        student.setId(1L);
        student.setName("Alice");
        student.setGrades(null);
        student.setSchoolClass(null);

        GPA gpa = buildGPA(1L, 25.0, 25.0, 35.0, 85.0);
        gpa.setSubject(subject);
        gpa.setStudent(student);

        when(gpaService.findById(1L)).thenReturn(Optional.of(gpa));

        mockMvc.perform(get("/gpa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.g1").value(25.0))
                .andExpect(jsonPath("$.finalGrade").value(85.0));
    }

    @Test
    void findGPAById_shouldReturn404_whenNotFound() throws Exception {
        when(gpaService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/gpa/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_shouldReturn200WithList_whenNotEmpty() throws Exception {
        GPA gpa1 = buildGPA(1L, 25.0, 25.0, 35.0, 85.0);
        GPA gpa2 = buildGPA(2L, 20.0, 20.0, 30.0, 70.0);

        when(gpaService.findAll()).thenReturn(List.of(gpa1, gpa2));

        mockMvc.perform(get("/gpa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findAll_shouldReturn404_whenEmpty() throws Exception {
        when(gpaService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/gpa"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByStudentWithDetails_shouldReturn200_whenFound() throws Exception {
        GPA gpa = buildGPA(1L, 25.0, 25.0, 35.0, 85.0);

        when(gpaService.findByStudentWithDetails(1L)).thenReturn(List.of(gpa));

        mockMvc.perform(get("/gpa/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void findByStudentWithDetails_shouldReturn404_whenEmpty() throws Exception {
        when(gpaService.findByStudentWithDetails(99L)).thenReturn(List.of());

        mockMvc.perform(get("/gpa/student/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findFinalGradeByStudentAndSubject_shouldReturn200_whenFound() throws Exception {
        when(gpaService.findFinalGradeByStudentAndSubject(1L, 1L)).thenReturn(85.0);

        mockMvc.perform(get("/gpa/student/1/subject/1/final-grade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(85.0));
    }

    @Test
    void findFinalGradeByStudentAndSubject_shouldReturn404_whenNotFound() throws Exception {
        when(gpaService.findFinalGradeByStudentAndSubject(anyLong(), anyLong())).thenReturn(null);

        mockMvc.perform(get("/gpa/student/1/subject/99/final-grade"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllFinalGradesByStudent_shouldReturn200_whenFound() throws Exception {
        when(gpaService.findAllFinalGradesByStudent(1L)).thenReturn(List.of(85.0, 90.0));

        mockMvc.perform(get("/gpa/student/1/final-grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findAllFinalGradesByStudent_shouldReturn404_whenEmpty() throws Exception {
        when(gpaService.findAllFinalGradesByStudent(99L)).thenReturn(List.of());

        mockMvc.perform(get("/gpa/student/99/final-grades"))
                .andExpect(status().isNotFound());
    }
}

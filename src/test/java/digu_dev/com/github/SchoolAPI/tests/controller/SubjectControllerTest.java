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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import digu_dev.com.github.SchoolAPI.controller.SubjectController;
import digu_dev.com.github.SchoolAPI.dto.SubjectDto;
import digu_dev.com.github.SchoolAPI.entity.Subject;
import digu_dev.com.github.SchoolAPI.service.SubjectService;

@DataJpaTest
@ActiveProfiles("test")
@WebMvcTest(SubjectController.class)
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubjectService subjectService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Subject buildSubject(Long id, String name) {
        Subject subject = new Subject();
        subject.setId(id);
        subject.setName(name);
        subject.setTeacherSubject(new HashSet<>());
        subject.setGpaList(null);
        return subject;
    }

    @Test
    void createSubject_shouldReturn201_whenValidDto() throws Exception {
        SubjectDto dto = new SubjectDto(null, "Mathematics");

        Subject saved = buildSubject(1L, "Mathematics");

        when(subjectService.create(any(SubjectDto.class))).thenReturn(saved);

        mockMvc.perform(post("/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateSubject_shouldReturn200_whenFound() throws Exception {
        Subject existing = buildSubject(1L, "Old Name");
        Subject updated = buildSubject(1L, "New Name");

        when(subjectService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(subjectService).update(any(Subject.class));

        mockMvc.perform(put("/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());
    }

    @Test
    void updateSubject_shouldReturn404_whenNotFound() throws Exception {
        Subject subject = buildSubject(null, "Math");

        when(subjectService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/subjects/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subject)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSubject_shouldReturn200_whenFound() throws Exception {
        Subject existing = buildSubject(1L, "Math");

        when(subjectService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(subjectService).delete(any(Subject.class));

        mockMvc.perform(delete("/subjects/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSubject_shouldReturn404_whenNotFound() throws Exception {
        when(subjectService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/subjects/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSubjectById_shouldReturn200WithDto_whenFound() throws Exception {
        Subject subject = buildSubject(1L, "Physics");

        when(subjectService.findById(1L)).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Physics"));
    }

    @Test
    void getSubjectById_shouldReturn404_whenNotFound() throws Exception {
        when(subjectService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/subjects/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllSubjects_shouldReturn200WithList_whenNotEmpty() throws Exception {
        Subject s1 = buildSubject(1L, "Math");
        Subject s2 = buildSubject(2L, "Biology");

        when(subjectService.findAll()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllSubjects_shouldReturn204_whenEmpty() throws Exception {
        when(subjectService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getSubjectByNameWithTeachers_shouldReturn200_whenFound() throws Exception {
        Subject subject = buildSubject(1L, "Chemistry");

        when(subjectService.findByNameWithTeachers("Chemistry")).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/subjects/name/Chemistry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Chemistry"));
    }

    @Test
    void getSubjectByNameWithTeachers_shouldReturn404_whenNotFound() throws Exception {
        when(subjectService.findByNameWithTeachers("Unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/subjects/name/Unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSubjectByName_shouldReturn200_whenFound() throws Exception {
        Subject subject = buildSubject(1L, "History");

        when(subjectService.findByName("History")).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/subjects/nameSubject/History"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("History"));
    }

    @Test
    void getSubjectByName_shouldReturn404_whenNotFound() throws Exception {
        when(subjectService.findByName("Unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/subjects/nameSubject/Unknown"))
                .andExpect(status().isNotFound());
    }
}

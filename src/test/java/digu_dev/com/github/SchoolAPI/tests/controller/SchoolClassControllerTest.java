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

import digu_dev.com.github.SchoolAPI.controller.SchoolClassController;
import digu_dev.com.github.SchoolAPI.dto.SchoolClassDto;
import digu_dev.com.github.SchoolAPI.entity.SchoolClass;
import digu_dev.com.github.SchoolAPI.entity.Student;
import digu_dev.com.github.SchoolAPI.service.SchoolClassService;

@WebMvcTest(SchoolClassController.class)
class SchoolClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SchoolClassService schoolClassService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private SchoolClass buildSchoolClass(Long id, String classCode) {
        SchoolClass sc = new SchoolClass();
        sc.setId(id);
        sc.setClassCode(classCode);
        sc.setStudents(null);
        return sc;
    }

    @Test
    void createSchoolClass_shouldReturn201_whenValidDto() throws Exception {
        SchoolClassDto dto = new SchoolClassDto(null, "A1");

        SchoolClass saved = buildSchoolClass(1L, "A1");

        when(schoolClassService.create(any(SchoolClassDto.class))).thenReturn(saved);

        mockMvc.perform(post("/school-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateSchoolClass_shouldReturn200_whenFound() throws Exception {
        SchoolClassDto dto = new SchoolClassDto(null, "B2");
        SchoolClass existing = buildSchoolClass(1L, "A1");

        when(schoolClassService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(schoolClassService).update(any(SchoolClass.class));

        mockMvc.perform(put("/school-classes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateSchoolClass_shouldReturn404_whenNotFound() throws Exception {
        SchoolClassDto dto = new SchoolClassDto(null, "B2");

        when(schoolClassService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/school-classes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteSchoolClass_shouldReturn204_whenFound() throws Exception {
        SchoolClass existing = buildSchoolClass(1L, "A1");

        when(schoolClassService.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(schoolClassService).delete(any(SchoolClass.class));

        mockMvc.perform(delete("/school-classes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteSchoolClass_shouldReturn404_whenNotFound() throws Exception {
        when(schoolClassService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/school-classes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSchoolClassById_shouldReturn200WithDto_whenFound() throws Exception {
        SchoolClass sc = buildSchoolClass(1L, "C3");

        when(schoolClassService.findById(1L)).thenReturn(Optional.of(sc));

        mockMvc.perform(get("/school-classes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.classCode").value("C3"));
    }

    @Test
    void getSchoolClassById_shouldReturn404_whenNotFound() throws Exception {
        when(schoolClassService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/school-classes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllSchoolClasses_shouldReturn200WithList_whenNotEmpty() throws Exception {
        SchoolClass sc1 = buildSchoolClass(1L, "A1");
        SchoolClass sc2 = buildSchoolClass(2L, "B2");

        when(schoolClassService.findAll()).thenReturn(List.of(sc1, sc2));

        mockMvc.perform(get("/school-classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllSchoolClasses_shouldReturn204_whenEmpty() throws Exception {
        when(schoolClassService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/school-classes"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getSchoolClassByCode_shouldReturn200WithDto_whenFound() throws Exception {
        SchoolClass sc = buildSchoolClass(1L, "D4");

        when(schoolClassService.findByClassCode("D4")).thenReturn(Optional.of(sc));

        mockMvc.perform(get("/school-classes/code/D4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classCode").value("D4"));
    }

    @Test
    void getSchoolClassByCode_shouldReturn404_whenNotFound() throws Exception {
        when(schoolClassService.findByClassCode("ZZ")).thenReturn(Optional.empty());

        mockMvc.perform(get("/school-classes/code/ZZ"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByCodeWithStudents_shouldReturn200WithStudents_whenFound() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Alice");
        student.setRegistration("2024001");
        student.setSchoolClass(null);
        student.setGrades(null);

        SchoolClass sc = buildSchoolClass(1L, "E5");
        sc.setStudents(List.of(student));

        when(schoolClassService.findByCodeWithStudents("E5")).thenReturn(Optional.of(sc));

        mockMvc.perform(get("/school-classes/code/E5/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classCode").value("E5"))
                .andExpect(jsonPath("$.students.length()").value(1));
    }

    @Test
    void findByCodeWithStudents_shouldReturn404_whenNotFound() throws Exception {
        when(schoolClassService.findByCodeWithStudents("ZZ")).thenReturn(Optional.empty());

        mockMvc.perform(get("/school-classes/code/ZZ/students"))
                .andExpect(status().isNotFound());
    }
}

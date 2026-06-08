package digu_dev.com.github.SchoolAPI.tests.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest extends ControllerSecurityTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void currentUser_shouldReturn401_whenMissingToken() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void currentUser_shouldExposePrincipalData_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/auth/me").with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.authorities[0]").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.claims.preferred_username").value("admin"));
    }
}

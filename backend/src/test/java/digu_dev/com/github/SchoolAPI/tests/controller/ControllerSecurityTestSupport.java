package digu_dev.com.github.SchoolAPI.tests.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

abstract class ControllerSecurityTestSupport {

    @MockitoBean
    JwtDecoder jwtDecoder;

    protected JwtRequestPostProcessor adminJwt() {
        return jwtFor("admin", "admin");
    }

    protected JwtRequestPostProcessor teacherJwt() {
        return jwtFor("teacher", "teacher");
    }

    protected JwtRequestPostProcessor jwtFor(String username, String... roles) {
        GrantedAuthority[] authorities = Stream.of(roles)
                .map(role -> "ROLE_" + role.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .toArray(GrantedAuthority[]::new);

        return jwt()
                .jwt(token -> token
                        .claim("sub", username)
                        .claim("preferred_username", username)
                        .claim("realm_access", Map.of("roles", List.of(roles))))
                .authorities(authorities);
    }
}

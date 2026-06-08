package digu_dev.com.github.SchoolAPI.config;

import java.io.IOException;
import java.time.Instant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import digu_dev.com.github.SchoolAPI.exception.StandardError;
import digu_dev.com.github.SchoolAPI.security.KeycloakRealmRoleConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter)
            throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/auth/me").authenticated()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/users/**").hasRole("ADMIN")
                .requestMatchers("/teachers/**").hasRole("ADMIN")
                .requestMatchers("/gpa/**").hasRole("TEACHER")
                .requestMatchers("/departments/**", "/students/**", "/subjects/**", "/school-classes/**")
                .hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                .anyRequest().authenticated());
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        http.exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, exception) -> writeError(
                        request,
                        response,
                        HttpStatus.UNAUTHORIZED,
                        "Unauthorized",
                        "A valid Bearer token is required to access this resource."))
                .accessDeniedHandler((request, response, exception) -> writeError(
                        request,
                        response,
                        HttpStatus.FORBIDDEN,
                        "Forbidden",
                        "You do not have permission to access this resource.")));

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(KeycloakRealmRoleConverter keycloakRealmRoleConverter) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setPrincipalClaimName("preferred_username");
        converter.setJwtGrantedAuthoritiesConverter(keycloakRealmRoleConverter);
        return converter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private void writeError(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String error,
            String message) throws IOException {
        StandardError body = new StandardError(
                Instant.now(),
                status.value(),
                error,
                message,
                request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}

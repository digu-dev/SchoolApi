package digu_dev.com.github.SchoolAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import digu_dev.com.github.SchoolAPI.security.CustomUserDetailsService;
import digu_dev.com.github.SchoolAPI.service.UserService;

@Configuration
public class SecurityConfig {


    @Bean
    SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());    
    http.httpBasic(Customizer.withDefaults());
    http.formLogin(configurer -> configurer.loginPage("/login").permitAll());

    http.authorizeHttpRequests(authorize ->{
        authorize.requestMatchers("/login/**").permitAll();
        authorize.requestMatchers(HttpMethod.POST, "/users/**").permitAll();
        authorize.requestMatchers("/students/**", "/teachers/**", "/subject/**", "/school-classes/**").hasAnyRole("ADMIN");
        authorize.requestMatchers("/gpa/**").hasAnyRole("TEACHER");
        authorize.anyRequest().authenticated();
    });
    
    return http.build();
    }

    @Bean 
    public UserDetailsService userDetailsService(UserService userService){
        return new CustomUserDetailsService(userService);
    }

    @Bean 
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

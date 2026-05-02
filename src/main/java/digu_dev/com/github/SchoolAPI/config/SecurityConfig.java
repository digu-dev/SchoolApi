package digu_dev.com.github.SchoolAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import digu_dev.com.github.SchoolAPI.security.CustomUserDetailsService;
import digu_dev.com.github.SchoolAPI.service.UserService;

@Configuration
public class SecurityConfig {


    @Bean
    SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
    //http.requiresChannel(channel -> channel.anyRequest().requiresSecure()); -> only for https and prod enviroments
    /* //http.sessionManagement(smc -> smc.maximumSessions(1).maxSessionsPreventsLogin(true)); 
    session management for single session per user, can be used for logout and session invalidation*/
    http.csrf(csrf -> csrf.disable());    
    http.httpBasic(withDefaults());
    http.formLogin(withDefaults());
       http.authorizeHttpRequests((requests) -> requests
            .requestMatchers("/login/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/users/**", "/users").permitAll()
            .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
            .anyRequest().authenticated()
        );
       
       return http.build();
    }
    
    @Bean 
    public UserDetailsService userDetailsService(UserService userService){
        return new CustomUserDetailsService(userService);
    }

    @Bean 
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}

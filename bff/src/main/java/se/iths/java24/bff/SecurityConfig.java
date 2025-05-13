package se.iths.java24.bff;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/spa.html", "/js/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                //.oauth2Login(Customizer.withDefaults())
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/spa.html", true) // 👈 Force redirect here after login
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/spa.html") // redirect to SPA after logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }

}

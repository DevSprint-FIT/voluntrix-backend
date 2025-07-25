package com.DevSprint.voluntrix_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // Disable CSRF only for APIs
                .cors(cors -> {}) // Use the default CORS configuration from CorsConfig
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/swagger-ui/**", 
                            "/v3/api-docs/**",
                            "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/analytics/**").permitAll()
                        .requestMatchers("/api/payment/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll() // Allow all requests under `/api/public/`
                        .requestMatchers("/api/chat/**").permitAll() // Allow chat API endpoints
                        .requestMatchers("/api/private-chat/**").permitAll() // Allow private chat API endpoints
                        .requestMatchers("/ws/**").permitAll() // Allow WebSocket connections
                        .requestMatchers("/chat", "/chat.html", "/static/**", "/*.html", "/*.css", "/*.js", "/").permitAll() // Allow static resources and chat pages
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Restrict `/api/admin/` to ADMIN role
                        .anyRequest().authenticated() // Require authentication for everything else
                )
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}

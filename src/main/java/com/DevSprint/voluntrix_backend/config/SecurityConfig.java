package com.DevSprint.voluntrix_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // Disable CSRF only for APIs
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                            "/swagger-ui/**", 
                            "/v3/api-docs/**",
                            "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/analytics/**").permitAll()
                        .requestMatchers("/api/payment/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll() // Allow all requests under `/api/public/`
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Restrict `/api/admin/` to ADMIN role
                        .anyRequest().authenticated() // Require authentication for everything else
                )
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "https://voluntrix-preview.vercel.app", "https://1d49-2402-4000-2100-693d-a5c6-d62f-f9bc-2e42.ngrok-free.app")); // Allow local & production frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // Needed if frontend sends credentials (e.g., tokens)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public CorsFilter corsFilter() { // CORS Filter Bean
        return new CorsFilter(corsConfigurationSource());
    }
}

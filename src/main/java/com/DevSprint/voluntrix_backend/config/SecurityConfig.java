package com.DevSprint.voluntrix_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.DevSprint.voluntrix_backend.security.JwtAuthenticationFilter;
import com.DevSprint.voluntrix_backend.security.OAuth2SuccessHandler;
import com.DevSprint.voluntrix_backend.services.auth.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
<<<<<<< HEAD
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // Disable CSRF only for APIs
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html",
                            "/api/auth/signup",
                            "/api/auth/login", 
                            "/api/auth/verify-email",
                            "/api/auth/resend-verification"
                    ).permitAll()
                    .requestMatchers("/api/auth/**").authenticated() // Other auth endpoints need auth
                    .requestMatchers("/api/admin/**").hasRole("ADMIN") // Restrict `/api/admin/` to ADMIN role
                    .anyRequest().authenticated() // Require authentication for everything else
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session management4
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2SuccessHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
            .userDetailsService(customUserDetailsService) // Set custom user details service
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable());
        
=======
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

>>>>>>> feature/VOLUNTRIX-37/sponsorship-module
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "https://voluntrix-preview.vercel.app", "https://1d49-2402-4000-2100-693d-a5c6-d62f-f9bc-2e42.ngrok-free.app")); // Allow local & production frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // Needed if frontend sends credentials (e.g., tokens)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
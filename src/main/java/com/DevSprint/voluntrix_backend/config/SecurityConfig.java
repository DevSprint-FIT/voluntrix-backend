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

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.DevSprint.voluntrix_backend.security.JwtAuthenticationFilter;
import com.DevSprint.voluntrix_backend.security.OAuth2SuccessHandler;
import com.DevSprint.voluntrix_backend.services.auth.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/ws/**", "/app/**", "/topic/**", "/user/**")) // Disable CSRF for APIs and WebSocket
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html",
                            "/api/auth/signup",
                            "/api/auth/login", 
                            "/api/auth/verify-email",
                            "/api/auth/resend-verification",
                            "/api/public/**",
                            "/api/payment/**",
                            "/api/chat/**",
                            "/api/private-chat/**"
                    ).permitAll()
                    .requestMatchers("/ws/**").permitAll() // Allow all WebSocket and SockJS connections
                    .requestMatchers("/app/**").permitAll() // Allow STOMP application destination prefixes
                    .requestMatchers("/topic/**").permitAll() // Allow STOMP topic subscriptions
                    .requestMatchers("/user/**").permitAll() // Allow STOMP user-specific subscriptions
                    .requestMatchers("/chat", "/chat.html", "/static/**", "/*.html", "/*.css", "/*.js", "/").permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN") // Restrict `/api/admin/` to ADMIN role
                    .requestMatchers("/api/auth/**").authenticated() // Other auth endpoints need auth
                    .requestMatchers("/api/**").authenticated() // General API endpoints need auth
                    .anyRequest().authenticated() // Require authentication for everything else
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session management4
            .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google") // Explicit OAuth2 login page
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorization")) // Only handle OAuth2 for this specific endpoint
                        .successHandler(oAuth2SuccessHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
            .userDetailsService(customUserDetailsService) // Set custom user details service
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter(CorsConfigurationSource corsConfigurationSource) {
        return new CorsFilter(corsConfigurationSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

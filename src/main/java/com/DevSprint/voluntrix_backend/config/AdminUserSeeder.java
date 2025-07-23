package com.DevSprint.voluntrix_backend.config;

import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.AuthProvider;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Value("${app.admin.fullname}")
    private String adminFullName;

    @Value("${app.admin.handle}")
    private String adminHandle;

    @Override
    public void run(String... args) throws Exception {
        createAdminUserIfNotExists();
    }

    private void createAdminUserIfNotExists() {
        // Check if admin user already exists
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            log.info("Admin user already exists with email: {}", adminEmail);
            return;
        }

        // Validate that admin password is provided
        if (adminPassword == null || adminPassword.trim().isEmpty()) {
            log.warn("Admin password not provided in environment variables. Skipping admin user creation.");
            log.warn("Please set ADMIN_PASSWORD environment variable to create admin user.");
            return;
        }

        // Create admin user
        UserEntity adminUser = new UserEntity();
        adminUser.setEmail(adminEmail);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setFullName(adminFullName);
        adminUser.setHandle(adminHandle);
        adminUser.setRole(UserType.ADMIN);
        adminUser.setIsProfileCompleted(true);
        adminUser.setIsVerified(true);
        adminUser.setAuthProvider(AuthProvider.EMAIL);

        userRepository.save(adminUser);
        log.info("Admin user created successfully with email: {}", adminEmail);
        log.warn("Please change the default admin password after first login!");
    }
}

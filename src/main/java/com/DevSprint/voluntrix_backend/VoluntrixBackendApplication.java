package com.DevSprint.voluntrix_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.DevSprint.voluntrix_backend.repositories")
public class VoluntrixBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(VoluntrixBackendApplication.class, args);
	}
}

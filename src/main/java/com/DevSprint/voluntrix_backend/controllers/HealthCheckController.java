package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/public/health-check")
@SecurityRequirement(name = "bearerAuth")
public class HealthCheckController {
    @GetMapping
    public ResponseEntity<String> healthCheck() {
        String message = "voluntrix-backend is running successfully";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

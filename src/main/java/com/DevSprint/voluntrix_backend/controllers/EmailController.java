package com.DevSprint.voluntrix_backend.controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import com.DevSprint.voluntrix_backend.dtos.EmailRequestDTO;
import com.DevSprint.voluntrix_backend.services.EmailService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/email")
@Validated
public class EmailController {
    
    private final EmailService emailService;

    @PostMapping("/send-html-email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequestDTO request) {
        try {
            emailService.sendEmailService(
                    request.getTo(),
                    request.getName(),
                    request.getOrderId(),
                    request.getAmount()
            );
            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException | IOException e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }
}
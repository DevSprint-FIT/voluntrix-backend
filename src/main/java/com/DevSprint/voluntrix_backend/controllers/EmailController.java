package com.DevSprint.voluntrix_backend.controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.DevSprint.voluntrix_backend.dtos.EmailRequestDto;
import com.DevSprint.voluntrix_backend.services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/public/email")
@Validated
public class EmailController {
    
    private final EmailService emailService;
    
    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-html-email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequestDto request) {
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
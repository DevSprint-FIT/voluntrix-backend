package com.DevSprint.voluntrix_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.EmailRequestDto;
import com.DevSprint.voluntrix_backend.services.EmailService;

@RestController
@RequestMapping("/api/public/email")
public class EmailController {
    
    @Autowired
    private final EmailService emailService;
    
    EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-html-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDto request) {
        boolean status = emailService.sendEmailService(
                request.getTo(),
                request.getName(),
                request.getOrderId(),
                request.getAmount()
        );

        if(status) {
            return ResponseEntity.ok("Email sent successfully!");
        }

        return ResponseEntity.ok("Email not sent");
    }
}
package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.VerificationRequestDTO;
import com.DevSprint.voluntrix_backend.services.RecaptchaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/verify")
public class PhoneVerificationController {
    private final RecaptchaService recaptchaService;

    public PhoneVerificationController(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @PostMapping
    public ResponseEntity<?> verifyPhone(@RequestBody VerificationRequestDTO request) {
        boolean isHuman = recaptchaService.verifyCaptcha(request.getCaptchaToken());

        if (!isHuman) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Captcha failed");
        }

        // Proceed with phone verification logic
        return ResponseEntity.ok("Verification code sent to " + request.getPhone());
    }
}

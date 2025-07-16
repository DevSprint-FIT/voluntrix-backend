package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevSprint.voluntrix_backend.dtos.InstituteOTPVerificationDTO;
import com.DevSprint.voluntrix_backend.dtos.InstituteVerificationRequestDTO;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.enums.UserType;
import com.DevSprint.voluntrix_backend.services.EmailVerificationService;
import com.DevSprint.voluntrix_backend.services.auth.CurrentUserService;
import com.DevSprint.voluntrix_backend.utils.ApiResponse;
import com.DevSprint.voluntrix_backend.validation.RequiresRole;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/volunteers/institute-verification")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class InstituteVerificationController {

    private final EmailVerificationService emailVerificationService;
    private final CurrentUserService currentUserService;

    @PostMapping("/send-otp")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<ApiResponse<String>> sendInstituteVerificationOTP(
            @Valid @RequestBody InstituteVerificationRequestDTO request) {
        
        Long userId = currentUserService.getCurrentUserId();
        UserEntity user = currentUserService.getCurrentUser(userId);
        
        emailVerificationService.sendInstituteVerificationEmail(
            user, 
            request.getInstituteEmail(), 
            request.getInstitute()
        );
        
        return ResponseEntity.ok(new ApiResponse<>(
            "OTP sent successfully", 
            "Verification code has been sent to " + request.getInstituteEmail()
        ));
    }

    @PostMapping("/verify-otp")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<ApiResponse<String>> verifyInstituteOTP(
            @Valid @RequestBody InstituteOTPVerificationDTO request) {
        
        Long userId = currentUserService.getCurrentUserId();
        UserEntity user = currentUserService.getCurrentUser(userId);
        
        boolean isValid = emailVerificationService.verifyInstituteOTP(
            user,
            request.getOtp()
        );
        
        if (isValid) {
            return ResponseEntity.ok(new ApiResponse<>(
                "Institute email verified successfully",
                "You can now proceed with volunteer registration"
            ));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                "Invalid or expired OTP",
                "Please request a new OTP and try again"
            ));
        }
    }

    @PostMapping("/resend-otp")
    @RequiresRole(UserType.VOLUNTEER)
    public ResponseEntity<ApiResponse<String>> resendInstituteVerificationOTP(
            @Valid @RequestBody InstituteVerificationRequestDTO request) {
        
        Long userId = currentUserService.getCurrentUserId();
        UserEntity user = currentUserService.getCurrentUser(userId);
        
        emailVerificationService.sendInstituteVerificationEmail(
            user,
            request.getInstituteEmail(),
            request.getInstitute()
        );
        
        return ResponseEntity.ok(new ApiResponse<>(
            "OTP resent successfully",
            "New verification code has been sent to " + request.getInstituteEmail()
        ));
    }
}

package com.DevSprint.voluntrix_backend.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DevSprint.voluntrix_backend.entities.EmailVerificationEntity;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.repositories.EmailVerificationRepository;
import com.DevSprint.voluntrix_backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();
    
    private static final int OTP_EXPIRY_MINUTES = 10;
    
    public String generateOTP() {
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }
    
    @Transactional
    public void sendVerificationEmail(UserEntity user) {
        try {
            // Delete any existing OTPs for this user
            emailVerificationRepository.deleteByUser(user.getUserId());
            
            // Generate new OTP
            String otp = generateOTP();
            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);
            
            EmailVerificationEntity verification = EmailVerificationEntity.builder()
                .user(user)
                .otp(otp)
                .expiresAt(expiresAt)
                .build();
            
            // Save OTP to database first
            EmailVerificationEntity savedVerification = emailVerificationRepository.save(verification);
            System.out.println("OTP saved to database: " + savedVerification.getId());
            
            // Send email
            emailService.sendVerificationEmail(user.getEmail(), user.getFullName(), otp);
            System.out.println("Verification email sent to: " + user.getEmail());
            
        } catch (Exception e) {
            System.err.println("Error in sendVerificationEmail: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send verification email: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public boolean verifyOTP(UserEntity user, String otp) {
        // Clean up expired OTPs
        emailVerificationRepository.deleteExpiredOtps(LocalDateTime.now());
        
        var verification = emailVerificationRepository.findByUserAndOtpAndIsUsedFalseAndExpiresAtAfter(
            user, otp, LocalDateTime.now()
        );
        
        if (verification.isPresent()) {
            EmailVerificationEntity entity = verification.get();
            
            // Mark OTP as used
            entity.setIsUsed(true);
            entity.setVerifiedAt(LocalDateTime.now());
            emailVerificationRepository.save(entity);
            
            // Update user as verified
            user.setIsVerified(true);
            userRepository.save(user);
            
            return true;
        }
        
        return false;
    }
    
    public void cleanupExpiredOtps() {
        emailVerificationRepository.deleteExpiredOtps(LocalDateTime.now());
    }
}

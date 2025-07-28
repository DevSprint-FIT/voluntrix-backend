package com.DevSprint.voluntrix_backend.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.DevSprint.voluntrix_backend.entities.EmailVerificationEntity;
import com.DevSprint.voluntrix_backend.entities.UserEntity;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, Long> {
    
    Optional<EmailVerificationEntity> findByUserAndOtpAndIsUsedFalseAndExpiresAtAfter(
        UserEntity user, String otp, LocalDateTime currentTime
    );
    
    @Modifying
    @Transactional
    @Query("DELETE FROM EmailVerificationEntity e WHERE e.user.userId = :userId")
    void deleteByUser(@Param("userId") Long userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM EmailVerificationEntity e WHERE e.expiresAt < :currentTime")
    void deleteExpiredOtps(@Param("currentTime") LocalDateTime currentTime);
}

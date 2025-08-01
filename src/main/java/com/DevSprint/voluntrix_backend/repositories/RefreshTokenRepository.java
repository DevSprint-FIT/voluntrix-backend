package com.DevSprint.voluntrix_backend.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.DevSprint.voluntrix_backend.entities.RefreshTokenEntity;
import com.DevSprint.voluntrix_backend.entities.UserEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    
    Optional<RefreshTokenEntity> findByToken(String token);
    
    Optional<RefreshTokenEntity> findByUser(UserEntity user);
    
    boolean existsByToken(String token);
    
    @Modifying
    @Transactional
    void deleteByUser(UserEntity user);
    
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.expiryDate < :currentTime")
    int deleteExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
}

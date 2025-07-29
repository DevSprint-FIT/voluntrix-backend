package com.DevSprint.voluntrix_backend.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.DevSprint.voluntrix_backend.entities.RefreshTokenEntity;
import com.DevSprint.voluntrix_backend.entities.UserEntity;
import com.DevSprint.voluntrix_backend.exceptions.TokenRefreshException;
import com.DevSprint.voluntrix_backend.repositories.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration:604800000}") // 7 days default
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenEntity findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found"));
    }

    @Transactional
    public RefreshTokenEntity createRefreshToken(UserEntity user) {
        // Delete existing refresh token for this user
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000))
                .createdAt(LocalDateTime.now())
                .build();

        refreshToken = refreshTokenRepository.save(refreshToken);
        log.info("Created refresh token for user: {}", user.getEmail());
        return refreshToken;
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please make a new signin request");
        }

        // Update last used timestamp
        refreshTokenRepository.updateLastUsedAt(token.getToken(), LocalDateTime.now());
        return token;
    }

    @Transactional
    public void deleteByUser(UserEntity user) {
        refreshTokenRepository.deleteByUser(user);
        log.info("Deleted refresh token for user: {}", user.getEmail());
    }

    @Transactional
    public void deleteByToken(String token) {
        RefreshTokenEntity refreshToken = findByToken(token);
        refreshTokenRepository.delete(refreshToken);
        log.info("Deleted refresh token: {}", token);
    }

    @Transactional
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
        log.info("Cleaned up expired refresh tokens");
    }
}

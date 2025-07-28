package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/public/imagekit")
public class ImageKitController {

    @Value("${imagekit.publicKey}")
    private String publicKey;

    @Value("${imagekit.privateKey}")
    private String privateKey;

    @GetMapping("/auth")
    public ResponseEntity<Map<String, String>> getAuthenticationParams() throws Exception {
        long expire = Instant.now().getEpochSecond() + 60; // 1-minute expiry
        String token = UUID.randomUUID().toString();
        String raw = token + expire;

        // HMAC-SHA256 signature
        Mac hmac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
        hmac.init(secretKey);
        byte[] signatureBytes = hmac.doFinal(raw.getBytes(StandardCharsets.UTF_8));
        String signature = HexFormat.of().formatHex(signatureBytes);

        Map<String, String> authParams = new HashMap<>();
        authParams.put("token", token);
        authParams.put("expire", String.valueOf(expire));
        authParams.put("signature", signature);
        authParams.put("publicKey", publicKey);

        return ResponseEntity.ok(authParams);
    }
}

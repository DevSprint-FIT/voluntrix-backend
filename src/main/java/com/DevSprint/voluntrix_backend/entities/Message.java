package com.DevSprint.voluntrix_backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderId;
    
    @Column(nullable = false)
    private String receiverId;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    private String senderName;
    
    @Enumerated(EnumType.STRING)
    private MessageType type = MessageType.CHAT;
    
    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.SENT;
    
    private LocalDateTime deliveredAt;
    private LocalDateTime readAt;

    public enum MessageType {
        CHAT, FILE, IMAGE, VIDEO, AUDIO
    }

    public enum MessageStatus {
        SENT, DELIVERED, READ
    }
}

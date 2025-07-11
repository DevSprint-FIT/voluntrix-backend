package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private String id;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String content;
    private MessageType type;
    private LocalDateTime timestamp;
    private MessageStatus status;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        TYPING,
        STOP_TYPING
    }

    public enum MessageStatus {
        SENT,
        DELIVERED,
        READ
    }
}

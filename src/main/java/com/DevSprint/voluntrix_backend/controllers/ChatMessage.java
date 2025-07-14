package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.ChatMessageDTO.MessageType;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String content;
    private String sender;
    private String senderId;        // Add this - required for user identification
    private String senderName;      // Add this - required for display name
    private String receiver;        // Add this - for direct messages
    private String receiverId;      // Add this - for recipient identification
    private MessageType type;
    private LocalDateTime timestamp; // Add this - for message ordering
    private String messageId;       // Add this - for message tracking
    private boolean isRead;         // Add this - for read status
    private String roomId;          // Add this - for group chats (optional)

}
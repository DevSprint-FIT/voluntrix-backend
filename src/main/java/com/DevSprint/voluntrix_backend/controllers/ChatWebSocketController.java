package com.DevSprint.voluntrix_backend.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.DevSprint.voluntrix_backend.dtos.ChatMessageDTO;
import com.DevSprint.voluntrix_backend.dtos.UserStatusDTO;
import com.DevSprint.voluntrix_backend.services.ChatService;
import com.DevSprint.voluntrix_backend.services.UserSessionService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final UserSessionService userSessionService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessage) {
        try {
            // Set timestamp and generate ID if not present
            if (chatMessage.getTimestamp() == null) {
                chatMessage.setTimestamp(LocalDateTime.now());
            }
            if (chatMessage.getId() == null) {
                chatMessage.setId(UUID.randomUUID().toString());
            }

            // Save message to database
            chatService.saveMessage(chatMessage);

            // Send message to receiver
            messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiverId(),
                "/topic/messages",
                chatMessage
            );

            // Send delivery confirmation to sender
            ChatMessageDTO deliveryConfirmation = new ChatMessageDTO();
            deliveryConfirmation.setId(chatMessage.getId());
            deliveryConfirmation.setStatus(ChatMessageDTO.MessageStatus.DELIVERED);
            deliveryConfirmation.setTimestamp(LocalDateTime.now());
            
            messagingTemplate.convertAndSendToUser(
                chatMessage.getSenderId(),
                "/topic/message-status",
                deliveryConfirmation
            );

            log.info("Message sent from {} to {}", chatMessage.getSenderId(), chatMessage.getReceiverId());
            
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage(), e);
        }
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDTO addUser(@Payload ChatMessageDTO chatMessage,
                                 SimpMessageHeaderAccessor headerAccessor) {
        
        // Add user to session
        String sessionId = headerAccessor.getSessionId();
        userSessionService.addUserSession(chatMessage.getSenderId(), sessionId, chatMessage.getSenderName());
        
        // Store username in session
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes != null) {
            sessionAttributes.put("userId", chatMessage.getSenderId());
            sessionAttributes.put("username", chatMessage.getSenderName());
        }

        // Broadcast user status
        broadcastUserStatus(chatMessage.getSenderId(), chatMessage.getSenderName(), UserStatusDTO.UserStatus.ONLINE);

        chatMessage.setType(ChatMessageDTO.MessageType.JOIN);
        chatMessage.setTimestamp(LocalDateTime.now());
        
        return chatMessage;
    }

    @MessageMapping("/chat.typing")
    public void userTyping(@Payload ChatMessageDTO chatMessage) {
        chatMessage.setType(ChatMessageDTO.MessageType.TYPING);
        chatMessage.setTimestamp(LocalDateTime.now());
        
        messagingTemplate.convertAndSendToUser(
            chatMessage.getReceiverId(),
            "/topic/typing",
            chatMessage
        );
    }

    @MessageMapping("/chat.stopTyping")
    public void userStopTyping(@Payload ChatMessageDTO chatMessage) {
        chatMessage.setType(ChatMessageDTO.MessageType.STOP_TYPING);
        chatMessage.setTimestamp(LocalDateTime.now());
        
        messagingTemplate.convertAndSendToUser(
            chatMessage.getReceiverId(),
            "/topic/typing",
            chatMessage
        );
    }

    @MessageMapping("/chat.markAsRead")
    public void markMessageAsRead(@Payload ChatMessageDTO chatMessage) {
        try {
            // Update message status in database
            chatService.markMessageAsRead(chatMessage.getId());

            // Send read confirmation to sender
            ChatMessageDTO readConfirmation = new ChatMessageDTO();
            readConfirmation.setId(chatMessage.getId());
            readConfirmation.setStatus(ChatMessageDTO.MessageStatus.READ);
            readConfirmation.setTimestamp(LocalDateTime.now());
            
            messagingTemplate.convertAndSendToUser(
                chatMessage.getSenderId(),
                "/topic/message-status",
                readConfirmation
            );

        } catch (Exception e) {
            log.error("Error marking message as read: {}", e.getMessage(), e);
        }
    }

    private void broadcastUserStatus(String userId, String userName, UserStatusDTO.UserStatus status) {
        UserStatusDTO userStatus = new UserStatusDTO();
        userStatus.setUserId(userId);
        userStatus.setUserName(userName);
        userStatus.setStatus(status);
        
        messagingTemplate.convertAndSend("/topic/user-status", userStatus);
    }
}

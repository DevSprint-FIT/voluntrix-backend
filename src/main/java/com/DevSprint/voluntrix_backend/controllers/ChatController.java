package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.DevSprint.voluntrix_backend.services.ChatService;
import com.DevSprint.voluntrix_backend.services.UserSessionService;
import com.DevSprint.voluntrix_backend.dtos.ChatMessageDTO;
import com.DevSprint.voluntrix_backend.entities.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final UserSessionService userSessionService;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            log.info("Received message from {} to {}: {}", chatMessage.getSenderId(), chatMessage.getReceiverId(), chatMessage.getContent());
            
            // Save message to database
            Message savedMessage = chatService.saveMessage(chatMessage);
            
            // Convert back to DTO for sending
            ChatMessageDTO responseMessage = convertToDTO(savedMessage);
            
            // Send to receiver's private channel
            messagingTemplate.convertAndSendToUser(
                chatMessage.getReceiverId(),
                "/topic/messages",
                responseMessage
            );
            
            // Send confirmation back to sender
            messagingTemplate.convertAndSendToUser(
                chatMessage.getSenderId(),
                "/topic/message-status",
                responseMessage
            );
            
            log.info("Message sent successfully with ID: {}", savedMessage.getId());
            
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
        }
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String sessionId = headerAccessor.getSessionId();
            
            // Add user session
            userSessionService.addUserSession(chatMessage.getSenderId(), sessionId, chatMessage.getSenderName());
            
            // Store in WebSocket session attributes
            headerAccessor.getSessionAttributes().put("userId", chatMessage.getSenderId());
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderName());
            
            log.info("User {} joined the chat with session {}", chatMessage.getSenderId(), sessionId);
            
            // Broadcast user status update
            messagingTemplate.convertAndSend("/topic/user-status", 
                java.util.Map.of("userId", chatMessage.getSenderId(), "status", "online", "userName", chatMessage.getSenderName())
            );
            
            return ChatMessage.builder()
                .content(chatMessage.getSenderName() + " joined the chat")
                .sender(chatMessage.getSenderName())
                .type(chatMessage.getType())
                .build();
                
        } catch (Exception e) {
            log.error("Error adding user: {}", e.getMessage());
            return ChatMessage.builder()
                .content("Error joining chat")
                .sender("System")
                .type(ChatMessageDTO.MessageType.JOIN)
                .build();
        }
    }

    @MessageMapping("/chat.typing")
    public void handleTyping(@Payload ChatMessageDTO typingMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // Send typing indicator to receiver
            messagingTemplate.convertAndSendToUser(
                typingMessage.getReceiverId(),
                "/topic/typing",
                typingMessage
            );
            
            log.debug("Typing indicator sent from {} to {}", typingMessage.getSenderId(), typingMessage.getReceiverId());
            
        } catch (Exception e) {
            log.error("Error handling typing indicator: {}", e.getMessage());
        }
    }

    @MessageMapping("/chat.stopTyping")
    public void handleStopTyping(@Payload ChatMessageDTO typingMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // Send stop typing indicator to receiver
            messagingTemplate.convertAndSendToUser(
                typingMessage.getReceiverId(),
                "/topic/typing",
                typingMessage
            );
            
            log.debug("Stop typing indicator sent from {} to {}", typingMessage.getSenderId(), typingMessage.getReceiverId());
            
        } catch (Exception e) {
            log.error("Error handling stop typing indicator: {}", e.getMessage());
        }
    }

    @MessageMapping("/chat.markAsRead")
    public void markMessageAsRead(@Payload java.util.Map<String, String> payload, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String messageId = payload.get("messageId");
            String userId = payload.get("userId");
            
            chatService.markMessageAsRead(messageId);
            
            // Notify sender that message was read
            messagingTemplate.convertAndSendToUser(
                userId,
                "/topic/message-status",
                java.util.Map.of("messageId", messageId, "status", "READ")
            );
            
            log.info("Message {} marked as read by user {}", messageId, userId);
            
        } catch (Exception e) {
            log.error("Error marking message as read: {}", e.getMessage());
        }
    }

    private ChatMessageDTO convertToDTO(Message message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId().toString());
        dto.setSenderId(message.getSenderId());
        dto.setReceiverId(message.getReceiverId());
        dto.setContent(message.getContent());
        dto.setSenderName(message.getSenderName());
        dto.setTimestamp(message.getTimestamp());
        
        // Convert Entity enum to DTO enum
        if (message.getType() != null) {
            switch (message.getType()) {
                case CHAT:
                    dto.setType(ChatMessageDTO.MessageType.CHAT);
                    break;
                default:
                    dto.setType(ChatMessageDTO.MessageType.CHAT);
            }
        }

        if (message.getStatus() != null) {
            switch (message.getStatus()) {
                case SENT:
                    dto.setStatus(ChatMessageDTO.MessageStatus.SENT);
                    break;
                case DELIVERED:
                    dto.setStatus(ChatMessageDTO.MessageStatus.DELIVERED);
                    break;
                case READ:
                    dto.setStatus(ChatMessageDTO.MessageStatus.READ);
                    break;
                default:
                    dto.setStatus(ChatMessageDTO.MessageStatus.SENT);
            }
        }

        return dto;
    }
}

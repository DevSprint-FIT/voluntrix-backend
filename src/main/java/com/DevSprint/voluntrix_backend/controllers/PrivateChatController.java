package com.DevSprint.voluntrix_backend.controllers;

import com.DevSprint.voluntrix_backend.dtos.ChatMessageDTO;
import com.DevSprint.voluntrix_backend.dtos.ConversationSummary;
import com.DevSprint.voluntrix_backend.entities.Message;
import com.DevSprint.voluntrix_backend.services.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PrivateChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    /**
     * Handle joining a private room
     */
    @MessageMapping("/private-room/{roomId}/join")
    public void joinPrivateRoom(@DestinationVariable String roomId, 
                               @Payload ChatMessageDTO message,
                               SimpMessageHeaderAccessor headerAccessor) {
        
        log.info("User {} joining private room: {}", message.getSenderName(), roomId);
        
        // Verify user can access this room
        if (!chatService.canAccessPrivateRoom(roomId, message.getSenderName())) {
            log.warn("User {} denied access to private room: {}", message.getSenderName(), roomId);
            return;
        }
        
        // Store username in session
        headerAccessor.getSessionAttributes().put("username", message.getSenderName());
        headerAccessor.getSessionAttributes().put("roomId", roomId);
        
        // Create join message
        ChatMessageDTO joinMessage = new ChatMessageDTO();
        joinMessage.setSenderName(message.getSenderName());
        joinMessage.setSenderId(message.getSenderId());
        joinMessage.setContent(message.getSenderName() + " joined the private conversation");
        joinMessage.setType(ChatMessageDTO.MessageType.CHAT);
        
        // Send join notification to the private room
        messagingTemplate.convertAndSend("/topic/private-room/" + roomId, joinMessage);
        
        log.info("User {} successfully joined private room: {}", message.getSenderName(), roomId);
    }

    /**
     * Handle sending a message in a private room
     */
    @MessageMapping("/private-room/{roomId}/send")
    public void sendPrivateMessage(@DestinationVariable String roomId,
                                  @Payload ChatMessageDTO message) {
        
        log.info("Sending private message to room: {} from user: {}", roomId, message.getSenderName());
        
        // Verify user can access this room
        if (!chatService.canAccessPrivateRoom(roomId, message.getSenderName())) {
            log.warn("User {} denied access to send message in private room: {}", message.getSenderName(), roomId);
            return;
        }
        
        try {
            // Save message to database with room ID
            Message savedMessage = chatService.saveMessage(message, roomId);
            
            // Create response DTO
            ChatMessageDTO responseMessage = new ChatMessageDTO();
            responseMessage.setId(savedMessage.getId().toString());
            responseMessage.setSenderName(message.getSenderName());
            responseMessage.setSenderId(message.getSenderId());
            responseMessage.setContent(message.getContent());
            responseMessage.setType(ChatMessageDTO.MessageType.CHAT);
            responseMessage.setTimestamp(savedMessage.getTimestamp());
            
            // Send message to all subscribers of this private room
            messagingTemplate.convertAndSend("/topic/private-room/" + roomId, responseMessage);
            
            log.info("Private message sent successfully to room: {}", roomId);
            
        } catch (Exception e) {
            log.error("Error sending private message to room: {}", roomId, e);
        }
    }
}

/**
 * REST controller for private room operations
 */
@RestController
@RequestMapping("/api/private-chat")
@RequiredArgsConstructor
@Slf4j
class PrivateChatRestController {

    private final ChatService chatService;

    /**
     * Create or get a private room for two users
     */
    @PostMapping("/create-room")
    public ResponseEntity<PrivateRoomResponse> createPrivateRoom(@RequestBody CreateRoomRequest request) {
        log.info("Creating private room for users: {} and {}", request.getUser1(), request.getUser2());
        
        try {
            String roomId = chatService.createOrGetPrivateRoom(request.getUser1(), request.getUser2());
            
            PrivateRoomResponse response = new PrivateRoomResponse();
            response.setRoomId(roomId);
            response.setUser1(request.getUser1());
            response.setUser2(request.getUser2());
            response.setSuccess(true);
            response.setMessage("Private room created successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating private room", e);
            
            PrivateRoomResponse response = new PrivateRoomResponse();
            response.setSuccess(false);
            response.setMessage("Failed to create private room: " + e.getMessage());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Get chat history for a private room
     */
    @GetMapping("/room/{roomId}/history")
    public ResponseEntity<List<ChatMessageDTO>> getPrivateRoomHistory(
            @PathVariable String roomId,
            @RequestParam String username) {
        
        log.info("Getting history for private room: {} and user: {}", roomId, username);
        
        try {
            List<ChatMessageDTO> history = chatService.getPrivateRoomHistory(roomId, username);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            log.error("Error getting private room history", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get all conversations for a user
     */
    @GetMapping("/conversations/{username}")
    public ResponseEntity<List<ConversationSummary>> getUserConversations(@PathVariable String username) {
        log.info("Getting conversations for user: {}", username);
        
        try {
            List<ConversationSummary> conversations = chatService.getUserConversations(username);
            return ResponseEntity.ok(conversations);
        } catch (Exception e) {
            log.error("Error getting user conversations", e);
            return ResponseEntity.status(500).build();
        }
    }

    // Request/Response DTOs
    public static class CreateRoomRequest {
        private String user1;
        private String user2;
        
        // Getters and setters
        public String getUser1() { return user1; }
        public void setUser1(String user1) { this.user1 = user1; }
        public String getUser2() { return user2; }
        public void setUser2(String user2) { this.user2 = user2; }
    }

    public static class PrivateRoomResponse {
        private String roomId;
        private String user1;
        private String user2;
        private boolean success;
        private String message;
        
        // Getters and setters
        public String getRoomId() { return roomId; }
        public void setRoomId(String roomId) { this.roomId = roomId; }
        public String getUser1() { return user1; }
        public void setUser1(String user1) { this.user1 = user1; }
        public String getUser2() { return user2; }
        public void setUser2(String user2) { this.user2 = user2; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}

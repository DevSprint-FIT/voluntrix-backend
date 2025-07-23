package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;

import com.DevSprint.voluntrix_backend.services.ChatService;
import com.DevSprint.voluntrix_backend.services.UserSessionService;
import com.DevSprint.voluntrix_backend.dtos.ChatMessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/public/chat") 
@RequiredArgsConstructor
@Slf4j
public class ChatRestController {

    private final ChatService chatService;
    private final UserSessionService userSessionService;

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(
            @RequestParam String user1,
            @RequestParam String user2) {
        
        try {
            log.info("Loading chat history between {} and {}", user1, user2);
            List<ChatMessageDTO> messages = chatService.getChatHistory(user1, user2);
            log.info("Found {} messages between {} and {}", messages.size(), user1, user2);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            log.error("Error loading chat history between {} and {}: {}", user1, user2, e.getMessage());
            return ResponseEntity.ok(new ArrayList<>()); // Return empty list instead of error to prevent frontend crash
        }
    }

    @GetMapping("/online-users")
    public ResponseEntity<List<Map<String, String>>> getOnlineUsers() {
        try {
            Set<String> onlineUserIds = userSessionService.getOnlineUsers();
            List<Map<String, String>> onlineUsers = new ArrayList<>();
            
            for (String userId : onlineUserIds) {
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("userId", userId);
                userInfo.put("userName", userSessionService.getUserName(userId));
                onlineUsers.add(userInfo);
            }
            
            log.info("Returning {} online users", onlineUsers.size());
            return ResponseEntity.ok(onlineUsers);
        } catch (Exception e) {
            log.error("Error loading online users: {}", e.getMessage());
            return ResponseEntity.ok(new ArrayList<>()); // Return empty list instead of error
        }
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<ChatMessageDTO>> getUnreadMessages(@PathVariable String userId) {
        try {
            List<ChatMessageDTO> unreadMessages = chatService.getUnreadMessages(userId);
            return ResponseEntity.ok(unreadMessages);
        } catch (Exception e) {
            log.error("Error loading unread messages for user {}: {}", userId, e.getMessage());
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/mark-read/{messageId}")
    public ResponseEntity<String> markMessageAsRead(@PathVariable String messageId) {
        try {
            chatService.markMessageAsRead(messageId);
            return ResponseEntity.ok("Message marked as read");
        } catch (Exception e) {
            log.error("Error marking message {} as read: {}", messageId, e.getMessage());
            return ResponseEntity.ok("Failed to mark as read");
        }
    }

    @GetMapping("/user-status/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStatus(@PathVariable String userId) {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("userId", userId);
            status.put("isOnline", userSessionService.isUserOnline(userId));
            status.put("userName", userSessionService.getUserName(userId));
            status.put("lastSeen", userSessionService.getLastSeen(userId));
            status.put("activeSessionCount", userSessionService.getActiveSessionCount(userId));
            
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            log.error("Error getting status for user {}: {}", userId, e.getMessage());
            Map<String, Object> errorStatus = new HashMap<>();
            errorStatus.put("userId", userId);
            errorStatus.put("isOnline", false);
            return ResponseEntity.ok(errorStatus);
        }
    }
    
    @GetMapping("/public-history")
    public ResponseEntity<List<ChatMessageDTO>> getPublicChatHistory(@RequestParam(defaultValue = "50") int limit) {
        try {
            log.info("Loading public chat history with limit: {}", limit);
            List<ChatMessageDTO> messages = chatService.getRecentPublicMessages(limit);
            log.info("Found {} public messages", messages.size());
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            log.error("Error loading public chat history: {}", e.getMessage());
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
}

package com.DevSprint.voluntrix_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DevSprint.voluntrix_backend.dtos.ChatMessageDTO;
import com.DevSprint.voluntrix_backend.dtos.UserStatusDTO;
import com.DevSprint.voluntrix_backend.services.ChatService;
import com.DevSprint.voluntrix_backend.services.UserSessionService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Configure properly in production
public class ChatRestController {

    private final ChatService chatService;
    private final UserSessionService userSessionService;

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(
            @RequestParam String user1,
            @RequestParam String user2) {
        List<ChatMessageDTO> messages = chatService.getChatHistory(user1, user2);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<ChatMessageDTO>> getUnreadMessages(@PathVariable String userId) {
        List<ChatMessageDTO> unreadMessages = chatService.getUnreadMessages(userId);
        return ResponseEntity.ok(unreadMessages);
    }

    @PostMapping("/mark-read/{messageId}")
    public ResponseEntity<String> markMessageAsRead(@PathVariable String messageId) {
        chatService.markMessageAsRead(messageId);
        return ResponseEntity.ok("Message marked as read");
    }

    @GetMapping("/online-users")
    public ResponseEntity<List<UserStatusDTO>> getOnlineUsers() {
        Set<String> onlineUserIds = userSessionService.getOnlineUsers();
        
        List<UserStatusDTO> onlineUsers = onlineUserIds.stream()
                .map(userId -> {
                    UserStatusDTO userStatus = new UserStatusDTO();
                    userStatus.setUserId(userId);
                    userStatus.setUserName(userSessionService.getUserName(userId));
                    userStatus.setStatus(UserStatusDTO.UserStatus.ONLINE);
                    return userStatus;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(onlineUsers);
    }

    @GetMapping("/user-status/{userId}")
    public ResponseEntity<UserStatusDTO> getUserStatus(@PathVariable String userId) {
        UserStatusDTO userStatus = new UserStatusDTO();
        userStatus.setUserId(userId);
        userStatus.setUserName(userSessionService.getUserName(userId));
        
        if (userSessionService.isUserOnline(userId)) {
            userStatus.setStatus(UserStatusDTO.UserStatus.ONLINE);
        } else {
            userStatus.setStatus(UserStatusDTO.UserStatus.OFFLINE);
            if (userSessionService.getLastSeen(userId) != null) {
                userStatus.setLastSeen(userSessionService.getLastSeen(userId).toString());
            }
        }
        
        return ResponseEntity.ok(userStatus);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Chat service is running");
    }
}

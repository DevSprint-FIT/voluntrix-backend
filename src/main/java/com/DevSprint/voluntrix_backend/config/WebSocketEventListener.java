package com.DevSprint.voluntrix_backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.DevSprint.voluntrix_backend.dtos.ChatMessageDTO;
import com.DevSprint.voluntrix_backend.dtos.UserStatusDTO;
import com.DevSprint.voluntrix_backend.services.UserSessionService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserSessionService userSessionService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("New WebSocket connection established");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String sessionId = headerAccessor.getSessionId();

        if (userId != null) {
            userSessionService.removeUserSession(userId, sessionId);
            
            // Check if user is completely offline
            if (!userSessionService.isUserOnline(userId)) {
                // Broadcast user offline status
                UserStatusDTO userStatus = new UserStatusDTO();
                userStatus.setUserId(userId);
                userStatus.setUserName(username);
                userStatus.setStatus(UserStatusDTO.UserStatus.OFFLINE);
                userStatus.setLastSeen(LocalDateTime.now().toString());
                
                messagingTemplate.convertAndSend("/topic/user-status", userStatus);

                // Send leave message
                ChatMessageDTO chatMessage = new ChatMessageDTO();
                chatMessage.setType(ChatMessageDTO.MessageType.LEAVE);
                chatMessage.setSenderId(userId);
                chatMessage.setSenderName(username);
                chatMessage.setTimestamp(LocalDateTime.now());
                
                messagingTemplate.convertAndSend("/topic/public", chatMessage);
                
                log.info("User {} disconnected", username);
            }
        }
    }
}

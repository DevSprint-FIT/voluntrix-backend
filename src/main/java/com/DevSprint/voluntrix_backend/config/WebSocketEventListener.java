package com.DevSprint.voluntrix_backend.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.DevSprint.voluntrix_backend.controllers.ChatMessage;
import com.DevSprint.voluntrix_backend.dtos.ChatMessageDTO.MessageType;
import com.DevSprint.voluntrix_backend.services.UserSessionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;
    private final UserSessionService userSessionService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String sessionId = headerAccessor.getSessionId();

        if (userId != null && username != null && sessionId != null) {
            // Remove user session
            userSessionService.removeUserSession(userId, sessionId);
            
            log.info("User {} disconnected with session {}", userId, sessionId);
            
            // Create disconnect message
            var chatMessage = ChatMessage.builder()
                .type(MessageType.LEAVE)
                .sender(username)
                .content(username + " left the chat")
                .build();

            // Broadcast disconnect message
            messageTemplate.convertAndSend("/topic/public", chatMessage);
            
            // Broadcast user status update
            messageTemplate.convertAndSend("/topic/user-status", 
                java.util.Map.of(
                    "userId", userId, 
                    "status", userSessionService.isUserOnline(userId) ? "online" : "offline", 
                    "userName", username
                )
            );
        }
    }
}

package com.DevSprint.voluntrix_backend.services;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class UserSessionService {
    
    private final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastSeenMap = new ConcurrentHashMap<>();
    private final Map<String, String> userNames = new ConcurrentHashMap<>();

    public void addUserSession(String userId, String sessionId, String userName) {
        userSessions.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
        userNames.put(userId, userName);
        log.info("User {} connected with session {}", userId, sessionId);
    }

    public void removeUserSession(String userId, String sessionId) {
        Set<String> sessions = userSessions.get(userId);
        if (sessions != null) {
            sessions.remove(sessionId);
            if (sessions.isEmpty()) {
                userSessions.remove(userId);
                lastSeenMap.put(userId, LocalDateTime.now());
                log.info("User {} disconnected, last session removed", userId);
            }
        }
    }

    public boolean isUserOnline(String userId) {
        Set<String> sessions = userSessions.get(userId);
        return sessions != null && !sessions.isEmpty();
    }

    public Set<String> getOnlineUsers() {
        return userSessions.keySet();
    }

    public LocalDateTime getLastSeen(String userId) {
        return lastSeenMap.get(userId);
    }

    public String getUserName(String userId) {
        return userNames.get(userId);
    }

    public int getActiveSessionCount(String userId) {
        Set<String> sessions = userSessions.get(userId);
        return sessions != null ? sessions.size() : 0;
    }
}

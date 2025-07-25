package com.DevSprint.voluntrix_backend.services;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class UserSessionService {
    
    private final Map<String, Set<String>> userSessions = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastSeenMap = new ConcurrentHashMap<>();
    private final Map<String, String> userNames = new ConcurrentHashMap<>();
    
    // Session management for privacy
    private String currentSessionId;
    private Set<String> currentSessionUsers = ConcurrentHashMap.newKeySet();
    private final Map<String, String> userNameToSessionMap = new ConcurrentHashMap<>(); // username -> latest session

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
    
    public Set<String> getAllKnownUsers() {
        return userNames.keySet();
    }
    
    public String getCurrentSessionId() {
        return currentSessionId;
    }
    
    public Set<String> getCurrentSessionUsers() {
        return new HashSet<>(currentSessionUsers);
    }
    
    public String getUserLatestSessionByName(String userName) {
        return userNameToSessionMap.get(userName);
    }
    
    public boolean shouldStartNewSession(String newUserId) {
        // Start a new session if:
        // 1. No current session exists
        // 2. The new user wasn't part of the previous session
        // 3. All previous users have left and this is a different user joining
        
        if (currentSessionId == null || currentSessionUsers.isEmpty()) {
            return true;
        }
        
        // Get the username of the new user
        String newUserName = getUserName(newUserId);
        if (newUserName == null) {
            // Extract username from userId (format: user_username)
            if (newUserId.startsWith("user_")) {
                newUserName = newUserId.substring(5); // Remove "user_" prefix
            }
        }
        
        if (newUserName == null) {
            log.warn("Could not extract username from userId: {}", newUserId);
            return true; // Start new session if we can't identify the user
        }
        
        // If the user was part of the current session users (by name), don't start new session
        for (String currentUser : currentSessionUsers) {
            String currentUserName = getUserName(currentUser);
            if (currentUserName == null && currentUser.startsWith("user_")) {
                currentUserName = currentUser.substring(5);
            }
            
            if (currentUserName != null && currentUserName.equals(newUserName)) {
                log.info("User {} is rejoining current session {}", newUserName, currentSessionId);
                return false;
            }
        }
        
        // If there are currently online users and this is a new user, start new session
        if (!getOnlineUsers().isEmpty()) {
            log.info("Starting new session because {} is a new user joining existing session", newUserName);
            return true;
        }
        
        return false;
    }
    
    public void startNewChatSession() {
        currentSessionId = "session_" + System.currentTimeMillis();
        currentSessionUsers.clear();
        log.info("Started new chat session: {}", currentSessionId);
    }
    
    public void addUserToCurrentSession(String userId) {
        if (currentSessionId == null) {
            startNewChatSession();
        }
        currentSessionUsers.add(userId);
        
        // Map username to session
        String userName = getUserName(userId);
        if (userName != null) {
            userNameToSessionMap.put(userName, currentSessionId);
        }
        
        log.info("Added user {} to session {}", userId, currentSessionId);
    }
    
    public void removeUserFromCurrentSession(String userId) {
        currentSessionUsers.remove(userId);
        log.info("Removed user {} from session {}", userId, currentSessionId);
        
        // If no users left in session, prepare for potential new session
        if (currentSessionUsers.isEmpty() && getOnlineUsers().isEmpty()) {
            log.info("Session {} is now empty", currentSessionId);
        }
    }
    
    public boolean canUserAccessSession(String userId, String sessionId) {
        String userName = getUserName(userId);
        if (userName == null && userId.startsWith("user_")) {
            userName = userId.substring(5);
        }
        
        if (userName != null) {
            String userLatestSession = userNameToSessionMap.get(userName);
            return sessionId.equals(userLatestSession);
        }
        
        return false;
    }
    
    // Restriction methods for two-user chat
    public boolean canAddNewUser() {
        return getOnlineUsers().size() < 2;
    }
    
    public int getOnlineUserCount() {
        return getOnlineUsers().size();
    }
    
    public boolean isChatRoomFull() {
        return getOnlineUsers().size() >= 2;
    }
}

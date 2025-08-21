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
        String session = userNameToSessionMap.get(userName);
        log.info("Looking for session for userName: {}, found: {}, current userNameToSessionMap: {}", 
                userName, session, userNameToSessionMap);
        return session;
    }
    
    public boolean shouldStartNewSession(String newUserId) {
        // Start a new session if:
        // 1. No current session exists
        // 2. The new user wasn't part of the previous session
        // 3. All previous users have left and this is a different user joining
        
        // Get the username of the new user first
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
        
        // FIRST: Check if this user has an existing session - this takes priority
        String userPreviousSession = userNameToSessionMap.get(newUserName);
        if (userPreviousSession != null) {
            // If the user has a previous session, continue it instead of starting new
            log.info("User {} has previous session {}, continuing it instead of starting new", newUserName, userPreviousSession);
            currentSessionId = userPreviousSession; // Continue the previous session
            return false;
        }
        
        // SECOND: If no current session exists, start a new one
        if (currentSessionId == null || currentSessionUsers.isEmpty()) {
            log.info("No current session or session is empty, starting new session for user {}", newUserName);
            return true;
        }
        
        // THIRD: Check if the user was part of the current session users (by name)
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
        
        // FOURTH: For a 2-user chat system, allow new users to join existing session if room isn't full
        // Only start new session if room is full (privacy protection)
        if (!getOnlineUsers().isEmpty()) {
            if (getOnlineUsers().size() >= 2) {
                log.info("Starting new session because chat room is full and {} is trying to join", newUserName);
                return true;
            } else {
                // Room has space for one more user - let them join the existing session
                log.info("Allowing {} to join existing session {} (room has space)", newUserName, currentSessionId);
                return false;
            }
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
        if (userName == null) {
            // Extract username from userId if not found in userNames map
            if (userId.startsWith("user_")) {
                userName = userId.substring(5);
                log.info("Extracted username '{}' from userId '{}'", userName, userId);
            }
        }
        
        log.info("Adding user {} (userId: {}) to session {}", userName, userId, currentSessionId);
        if (userName != null) {
            userNameToSessionMap.put(userName, currentSessionId);
            log.info("Updated userNameToSessionMap: {}", userNameToSessionMap);
        } else {
            log.warn("Could not find userName for userId: {}", userId);
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

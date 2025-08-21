package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "private_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String roomId;
    
    @Column(nullable = false)
    private String user1;
    
    @Column(nullable = false)
    private String user2;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "last_activity")
    private LocalDateTime lastActivity;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastActivity = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastActivity = LocalDateTime.now();
    }
    
    // Helper method to generate consistent room ID for two users
    public static String generateRoomId(String user1, String user2) {
        // Always put users in alphabetical order to ensure consistency
        String[] users = {user1, user2};
        java.util.Arrays.sort(users);
        return "room_" + users[0] + "_" + users[1];
    }
    
    // Check if a user belongs to this room
    public boolean hasUser(String username) {
        return user1.equals(username) || user2.equals(username);
    }
    
    // Get the other user in the room
    public String getOtherUser(String username) {
        if (user1.equals(username)) {
            return user2;
        } else if (user2.equals(username)) {
            return user1;
        }
        return null;
    }
}

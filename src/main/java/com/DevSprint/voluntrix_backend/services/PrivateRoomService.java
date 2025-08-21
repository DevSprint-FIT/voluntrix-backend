package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.PrivateRoom;
import com.DevSprint.voluntrix_backend.repositories.PrivateRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateRoomService {
    
    private final PrivateRoomRepository privateRoomRepository;
    
    /**
     * Create or get existing private room for two users
     */
    public PrivateRoom createOrGetPrivateRoom(String user1, String user2) {
        log.info("Creating or getting private room for users: {} and {}", user1, user2);
        
        // Check if room already exists
        Optional<PrivateRoom> existingRoom = privateRoomRepository.findRoomByUsers(user1, user2);
        if (existingRoom.isPresent()) {
            log.info("Found existing room: {}", existingRoom.get().getRoomId());
            return existingRoom.get();
        }
        
        // Create new room
        PrivateRoom newRoom = new PrivateRoom();
        newRoom.setRoomId(PrivateRoom.generateRoomId(user1, user2));
        newRoom.setUser1(user1);
        newRoom.setUser2(user2);
        
        PrivateRoom savedRoom = privateRoomRepository.save(newRoom);
        log.info("Created new private room: {}", savedRoom.getRoomId());
        
        return savedRoom;
    }
    
    /**
     * Check if user is allowed to access a room
     */
    public boolean canUserAccessRoom(String roomId, String username) {
        Optional<PrivateRoom> room = privateRoomRepository.findByRoomId(roomId);
        if (room.isPresent()) {
            return room.get().hasUser(username);
        }
        return false;
    }
    
    /**
     * Get all rooms for a user
     */
    public List<PrivateRoom> getUserRooms(String username) {
        return privateRoomRepository.findRoomsByUser(username);
    }
    
    /**
     * Get room by ID
     */
    public Optional<PrivateRoom> getRoomById(String roomId) {
        return privateRoomRepository.findByRoomId(roomId);
    }
    
    /**
     * Get private room for two users (if exists)
     */
    public Optional<PrivateRoom> getPrivateRoom(String user1, String user2) {
        return privateRoomRepository.findRoomByUsers(user1, user2);
    }
}

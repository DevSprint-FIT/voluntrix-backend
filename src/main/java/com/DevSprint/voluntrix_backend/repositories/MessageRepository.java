package com.DevSprint.voluntrix_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.DevSprint.voluntrix_backend.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Query("SELECT m FROM Message m WHERE " +
           "(m.senderId = :senderId AND m.receiverId = :receiverId) OR " +
           "(m.senderId = :receiverId AND m.receiverId = :senderId) " +
           "ORDER BY m.timestamp ASC")
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
        @Param("senderId") String senderId, 
        @Param("receiverId") String receiverId, 
        @Param("receiverId") String receiverId2, 
        @Param("senderId") String senderId2
    );
    
    List<Message> findByReceiverIdAndStatus(String receiverId, Message.MessageStatus status);
    
    @Query("SELECT m FROM Message m WHERE m.receiverId = :userId AND m.status != 'READ' ORDER BY m.timestamp DESC")
    List<Message> findUnreadMessagesByReceiverId(@Param("userId") String userId);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiverId = :userId AND m.status != 'read'")
    long countUnreadMessagesByReceiverId(@Param("userId") String userId);
    
    // Get public chat history for group chat
    @Query("SELECT m FROM Message m WHERE m.receiverId = 'public-room' ORDER BY m.timestamp ASC")
    List<Message> findPublicMessages();
    
    // Get recent public messages with limit
    @Query(value = "SELECT * FROM message WHERE receiver_id = 'public-room' ORDER BY timestamp DESC LIMIT :limit", nativeQuery = true)
    List<Message> findRecentPublicMessages(@Param("limit") int limit);
    
    // Session-based privacy queries
    @Query("SELECT m FROM Message m WHERE m.chatSessionId = :sessionId ORDER BY m.timestamp ASC")
    List<Message> findBySessionId(@Param("sessionId") String sessionId);
    
    @Query("SELECT m FROM Message m WHERE m.receiverId = 'public-room' AND m.chatSessionId = :sessionId ORDER BY m.timestamp ASC")
    List<Message> findPublicMessagesBySession(@Param("sessionId") String sessionId);
    
    @Query(value = "SELECT * FROM message WHERE receiver_id = 'public-room' AND chat_session_id = :sessionId ORDER BY timestamp DESC LIMIT :limit", nativeQuery = true)
    List<Message> findRecentPublicMessagesBySession(@Param("sessionId") String sessionId, @Param("limit") int limit);
    
    // For private room chat history
    @Query("SELECT m FROM Message m WHERE m.chatSessionId = :chatSessionId ORDER BY m.timestamp ASC")
    List<Message> findByChatSessionIdOrderByTimestampAsc(@Param("chatSessionId") String chatSessionId);
    
    // For private room messages ordered by timestamp
    @Query("SELECT m FROM Message m WHERE m.chatSessionId = :roomId ORDER BY m.timestamp DESC")
    List<Message> findByRoomIdOrderByTimestampDesc(@Param("roomId") String roomId);
    
    // Get latest message for a room
    @Query("SELECT m FROM Message m WHERE m.chatSessionId = :roomId ORDER BY m.timestamp DESC LIMIT 1")
    Message findLatestMessageByRoomId(@Param("roomId") String roomId);
}

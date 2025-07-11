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
}

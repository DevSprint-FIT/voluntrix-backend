package com.DevSprint.voluntrix_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DevSprint.voluntrix_backend.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
        String senderId, String receiverId, String receiverId2, String senderId2
    );
}

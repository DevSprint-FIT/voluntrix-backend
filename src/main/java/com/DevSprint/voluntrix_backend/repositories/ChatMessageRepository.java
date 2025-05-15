package com.DevSprint.voluntrix_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DevSprint.voluntrix_backend.entities.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiver(String sender, String receiver);
}
package com.DevSprint.voluntrix_backend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.MessageDTO;
import com.DevSprint.voluntrix_backend.entities.Message;
import com.DevSprint.voluntrix_backend.repositories.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(MessageDTO dto) {
        Message message = new Message();
        message.setSenderId(dto.getSenderId());
        message.setReceiverId(dto.getReceiverId());
        message.setContent(dto.getContent());
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getChatHistory(String user1, String user2) {
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            user1, user2, user1, user2
        );
    }
}

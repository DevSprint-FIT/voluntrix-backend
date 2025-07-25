package com.DevSprint.voluntrix_backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.ChatMessageDTO;
import com.DevSprint.voluntrix_backend.entities.Message;
import com.DevSprint.voluntrix_backend.repositories.MessageRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final MessageRepository messageRepository;

    public Message saveMessage(ChatMessageDTO chatMessageDTO) {
        Message message = new Message();
        message.setSenderId(chatMessageDTO.getSenderId());
        message.setReceiverId(chatMessageDTO.getReceiverId());
        message.setContent(chatMessageDTO.getContent());
        message.setSenderName(chatMessageDTO.getSenderName());
        message.setTimestamp(chatMessageDTO.getTimestamp() != null ? 
            chatMessageDTO.getTimestamp() : LocalDateTime.now());
        
        // Convert DTO enum to Entity enum
        if (chatMessageDTO.getType() != null) {
            switch (chatMessageDTO.getType()) {
                case CHAT:
                    message.setType(Message.MessageType.CHAT);
                    break;
                default:
                    message.setType(Message.MessageType.CHAT);
            }
        }

        if (chatMessageDTO.getStatus() != null) {
            switch (chatMessageDTO.getStatus()) {
                case SENT:
                    message.setStatus(Message.MessageStatus.SENT);
                    break;
                case DELIVERED:
                    message.setStatus(Message.MessageStatus.DELIVERED);
                    message.setDeliveredAt(LocalDateTime.now());
                    break;
                case READ:
                    message.setStatus(Message.MessageStatus.READ);
                    message.setReadAt(LocalDateTime.now());
                    break;
                default:
                    message.setStatus(Message.MessageStatus.SENT);
            }
        }

        Message savedMessage = messageRepository.save(message);
        log.info("Message saved with ID: {}", savedMessage.getId());
        return savedMessage;
    }

    public List<ChatMessageDTO> getChatHistory(String user1, String user2) {
        List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            user1, user2, user1, user2
        );
        
        return messages.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public void markMessageAsDelivered(String messageId) {
        try {
            Long id = Long.parseLong(messageId);
            Optional<Message> messageOpt = messageRepository.findById(id);
            if (messageOpt.isPresent()) {
                Message message = messageOpt.get();
                message.setStatus(Message.MessageStatus.DELIVERED);
                message.setDeliveredAt(LocalDateTime.now());
                messageRepository.save(message);
                log.info("Message {} marked as delivered", messageId);
            }
        } catch (NumberFormatException e) {
            log.error("Invalid message ID format: {}", messageId);
        }
    }

    public void markMessageAsRead(String messageId) {
        try {
            Long id = Long.parseLong(messageId);
            Optional<Message> messageOpt = messageRepository.findById(id);
            if (messageOpt.isPresent()) {
                Message message = messageOpt.get();
                message.setStatus(Message.MessageStatus.READ);
                message.setReadAt(LocalDateTime.now());
                messageRepository.save(message);
                log.info("Message {} marked as read", messageId);
            }
        } catch (NumberFormatException e) {
            log.error("Invalid message ID format: {}", messageId);
        }
    }

    public List<ChatMessageDTO> getUnreadMessages(String userId) {
        List<Message> unreadMessages = messageRepository.findByReceiverIdAndStatus(
            userId, Message.MessageStatus.DELIVERED
        );
        
        return unreadMessages.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ChatMessageDTO> getPublicChatHistory() {
        List<Message> messages = messageRepository.findPublicMessages();
        
        return messages.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ChatMessageDTO> getRecentPublicMessages(int limit) {
        List<Message> messages = messageRepository.findRecentPublicMessages(limit);
        
        // Reverse the order since we got them in DESC order
        Collections.reverse(messages);
        
        return messages.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private ChatMessageDTO convertToDTO(Message message) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId().toString());
        dto.setSenderId(message.getSenderId());
        dto.setReceiverId(message.getReceiverId());
        dto.setContent(message.getContent());
        dto.setSenderName(message.getSenderName());
        dto.setTimestamp(message.getTimestamp());
        
        // Convert Entity enum to DTO enum
        if (message.getType() != null) {
            switch (message.getType()) {
                case CHAT:
                    dto.setType(ChatMessageDTO.MessageType.CHAT);
                    break;
                default:
                    dto.setType(ChatMessageDTO.MessageType.CHAT);
            }
        }

        if (message.getStatus() != null) {
            switch (message.getStatus()) {
                case SENT:
                    dto.setStatus(ChatMessageDTO.MessageStatus.SENT);
                    break;
                case DELIVERED:
                    dto.setStatus(ChatMessageDTO.MessageStatus.DELIVERED);
                    break;
                case READ:
                    dto.setStatus(ChatMessageDTO.MessageStatus.READ);
                    break;
                default:
                    dto.setStatus(ChatMessageDTO.MessageStatus.SENT);
            }
        }

        return dto;
    }
}

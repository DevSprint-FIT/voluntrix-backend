package com.DevSprint.voluntrix_backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.DevSprint.voluntrix_backend.dtos.ChatMessageDTO;
import com.DevSprint.voluntrix_backend.dtos.ConversationSummary;
import com.DevSprint.voluntrix_backend.entities.Message;
import com.DevSprint.voluntrix_backend.entities.PrivateRoom;
import com.DevSprint.voluntrix_backend.repositories.MessageRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final MessageRepository messageRepository;
    private final PrivateRoomService privateRoomService;

    public Message saveMessage(ChatMessageDTO chatMessageDTO, String roomId) {
        log.info("Saving message to room: {}", roomId);
        
        // Get the private room to determine the receiver
        Optional<PrivateRoom> roomOpt = privateRoomService.getRoomById(roomId);
        if (!roomOpt.isPresent()) {
            throw new RuntimeException("Private room not found: " + roomId);
        }
        
        PrivateRoom room = roomOpt.get();
        
        // Determine receiver ID based on sender
        String receiverId = null;
        if (room.getUser1().equals(chatMessageDTO.getSenderName())) {
            receiverId = "user_" + room.getUser2();
        } else if (room.getUser2().equals(chatMessageDTO.getSenderName())) {
            receiverId = "user_" + room.getUser1();
        } else {
            throw new RuntimeException("Sender not authorized for room: " + roomId);
        }
        
        Message message = new Message();
        message.setSenderId(chatMessageDTO.getSenderId());
        message.setReceiverId(receiverId); // Set the receiver ID
        message.setContent(chatMessageDTO.getContent());
        message.setSenderName(chatMessageDTO.getSenderName());
        message.setTimestamp(chatMessageDTO.getTimestamp() != null ? 
            chatMessageDTO.getTimestamp() : LocalDateTime.now());
        
        // Use the specific room ID for this private conversation
        message.setChatSessionId(roomId);
        
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
        log.info("Message saved with ID: {} for private room: {}", savedMessage.getId(), roomId);
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
        // Simple approach - get all public messages
        List<Message> messages = messageRepository.findPublicMessages();
        
        return messages.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ChatMessageDTO> getRecentPublicMessages(int limit) {
        // Simple approach - get recent public messages
        List<Message> messages = messageRepository.findRecentPublicMessages(limit);
        
        // Reverse the order since we got them in DESC order
        Collections.reverse(messages);
        
        return messages.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ChatMessageDTO> getUserAccessibleChatHistory(String userName, int limit) {
        // Simple approach - get all public messages (no session filtering)
        List<Message> messages = messageRepository.findRecentPublicMessages(limit);
        Collections.reverse(messages);
        
        log.info("Loaded {} messages for user: {}", messages.size(), userName);
        
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
    
    /**
     * Get chat history for a specific private room
     */
    public List<ChatMessageDTO> getPrivateRoomHistory(String roomId, String username) {
        log.info("Loading private room history for room: {} and user: {}", roomId, username);
        
        // Verify user has access to this room
        if (!privateRoomService.canUserAccessRoom(roomId, username)) {
            log.warn("User {} does not have access to room: {}", username, roomId);
            return Collections.emptyList();
        }
        
        List<Message> messages = messageRepository.findByChatSessionIdOrderByTimestampAsc(roomId);
        
        log.info("Loaded {} messages for private room: {}", messages.size(), roomId);
        
        return messages.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Create or get private room for two users
     */
    public String createOrGetPrivateRoom(String user1, String user2) {
        PrivateRoom room = privateRoomService.createOrGetPrivateRoom(user1, user2);
        return room.getRoomId();
    }
    
    /**
     * Check if user can access a private room
     */
    public boolean canAccessPrivateRoom(String roomId, String username) {
        return privateRoomService.canUserAccessRoom(roomId, username);
    }
    
    /**
     * Get participants of a private room
     */
    public String[] getRoomParticipants(String roomId) {
        Optional<PrivateRoom> roomOpt = privateRoomService.getRoomById(roomId);
        if (roomOpt.isPresent()) {
            PrivateRoom room = roomOpt.get();
            return new String[]{room.getUser1(), room.getUser2()};
        }
        return new String[0];
    }
    
    /**
     * Get all conversations for a user with last message and unread count
     */
    public List<ConversationSummary> getUserConversations(String username) {
        log.info("Getting conversations for user: {}", username);
        
        List<PrivateRoom> userRooms = privateRoomService.getUserRooms(username);
        List<ConversationSummary> conversations = new ArrayList<>();
        
        for (PrivateRoom room : userRooms) {
            ConversationSummary summary = new ConversationSummary();
            
            summary.setRoomId(room.getRoomId());
            summary.setUser1(room.getUser1());
            summary.setUser2(room.getUser2());
            
            // Get last message for this room
            List<Message> roomMessages = messageRepository.findByRoomIdOrderByTimestampDesc(room.getRoomId());
            if (!roomMessages.isEmpty()) {
                Message lastMessage = roomMessages.get(0); // First one is the latest due to DESC order
                summary.setLastMessage(convertToDTO(lastMessage));
            }
            
            // Calculate unread count for this user in this room
            String currentUserId = "user_" + username;
            long unreadCount = messageRepository.countUnreadMessagesByRoomIdAndReceiverId(room.getRoomId(), currentUserId);
            summary.setUnreadCount((int) unreadCount);
            
            conversations.add(summary);
        }
        
        // Sort conversations by last message timestamp (most recent first)
        conversations.sort((a, b) -> {
            ChatMessageDTO lastMessageA = a.getLastMessage();
            ChatMessageDTO lastMessageB = b.getLastMessage();
            
            // If both have last messages, compare timestamps
            if (lastMessageA != null && lastMessageB != null && 
                lastMessageA.getTimestamp() != null && lastMessageB.getTimestamp() != null) {
                return lastMessageB.getTimestamp().compareTo(lastMessageA.getTimestamp()); // DESC order
            }
            
            // If only one has a last message, it should come first
            if (lastMessageA != null && lastMessageB == null) return -1;
            if (lastMessageA == null && lastMessageB != null) return 1;
            
            // If neither has messages, maintain original order
            return 0;
        });
        
        log.info("Found {} conversations for user: {}, sorted by last message timestamp", conversations.size(), username);
        return conversations;
    }
    
    /**
     * Mark all messages in a room as read for a specific user
     */
    public void markRoomMessagesAsRead(String roomId, String username) {
        log.info("Marking messages as read for user: {} in room: {}", username, roomId);
        
        String userId = "user_" + username;
        
        // Get all unread messages for this user in this room
        List<Message> unreadMessages = messageRepository.findByRoomIdOrderByTimestampDesc(roomId)
            .stream()
            .filter(message -> userId.equals(message.getReceiverId()) && 
                              message.getStatus() != Message.MessageStatus.READ)
            .collect(Collectors.toList());
        
        // Mark them as read
        for (Message message : unreadMessages) {
            message.setStatus(Message.MessageStatus.READ);
            message.setReadAt(LocalDateTime.now());
        }
        
        if (!unreadMessages.isEmpty()) {
            messageRepository.saveAll(unreadMessages);
            log.info("Marked {} messages as read for user: {} in room: {}", unreadMessages.size(), username, roomId);
        }
    }
}

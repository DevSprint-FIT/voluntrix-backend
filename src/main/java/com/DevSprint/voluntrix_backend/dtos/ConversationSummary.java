package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;

@Data
public class ConversationSummary {
    private String roomId;
    private String user1;
    private String user2;
    private ChatMessageDTO lastMessage;
    private int unreadCount;
}

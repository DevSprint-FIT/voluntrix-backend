package com.DevSprint.voluntrix_backend.dtos;

import lombok.Data;

@Data
public class MessageDTO {
    private String senderId;
    private String receiverId;
    private String content;
}

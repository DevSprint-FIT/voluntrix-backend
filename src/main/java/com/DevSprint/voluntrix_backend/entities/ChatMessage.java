package com.DevSprint.voluntrix_backend.entities;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class ChatMessage{

    @Id @GeneratedValue

    private Long id;
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp=LocalDateTime.now();

}

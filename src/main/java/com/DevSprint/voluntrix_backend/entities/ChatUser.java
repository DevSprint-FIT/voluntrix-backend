package com.DevSprint.voluntrix_backend.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class ChatUser {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private String password;
    
}

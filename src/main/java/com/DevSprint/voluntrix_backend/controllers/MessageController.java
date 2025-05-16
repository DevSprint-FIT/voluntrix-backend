package com.DevSprint.voluntrix_backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.DevSprint.voluntrix_backend.dtos.MessageDTO;
import com.DevSprint.voluntrix_backend.entities.Message;
import com.DevSprint.voluntrix_backend.services.MessageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody MessageDTO dto) {
        return ResponseEntity.ok(messageService.saveMessage(dto));
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(
        @RequestParam String user1,
        @RequestParam String user2
    ) {
        return ResponseEntity.ok(messageService.getChatHistory(user1, user2));
    }
}

// package com.DevSprint.voluntrix_backend.controllers;

// import com.DevSprint.voluntrix_backend.dtos.MessageDTO;
// import com.DevSprint.voluntrix_backend.entities.Message;
// import com.DevSprint.voluntrix_backend.services.MessageService;

// import lombok.RequiredArgsConstructor;

// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.stereotype.Controller;

// @Controller
// @RequiredArgsConstructor
// public class MessageWebSocketController {

//     private final SimpMessagingTemplate messagingTemplate;
//     private final MessageService messageService;

//     @MessageMapping("/chat/send")
//     public void sendMessage(MessageDTO dto) {
//         Message saved = messageService.saveMessage(dto);
//         messagingTemplate.convertAndSend("/topic/chat/" + dto.getReceiverId(), saved);
//     }
// }

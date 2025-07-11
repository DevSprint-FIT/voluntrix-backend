package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticResourceController {

    @GetMapping("/chat")
    public String chatPage() {
        return "forward:/chat.html";
    }
    
    @GetMapping("/")
    public String indexPage() {
        return "forward:/chat.html";
    }
}

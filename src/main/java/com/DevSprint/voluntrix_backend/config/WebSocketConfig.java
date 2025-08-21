package com.DevSprint.voluntrix_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(
                    "http://localhost:3000", 
                    "http://localhost:3001", 
                    "http://localhost:3002", 
                    "http://localhost:3003",
                    "https://localhost:8443",
                    "https://voluntrix-preview.vercel.app",
                    "https://voluntrix-frontend.vercel.app", 
                    "https://voluntrix-frontend-preview.vercel.app",
                    "https://voluntrix-devsprint.vercel.app"
                )
                .withSockJS()
                .setDisconnectDelay(30 * 1000);
    }
}
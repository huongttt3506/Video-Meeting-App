package com.example.video_meeting_app.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }


}
/*
registry.enableSimpleBroker("/topic"); send messages from server to client at endpoint
    If the server sends a message to /topic/notifications,
    every client that subscribes to /topic/notifications will receive the message.)

registry.setApplicationDestinationPrefixes("/app");
    prefix for messages from client send to server
    EX: Client send a message to /app/sendMessage
    Spring will remove the "/app" prefix
    and map the remaining part ("sendMessage") to a method in the controller (@MessageMapping("sendMessage"))
    Server -> client /topic/chatRoom
 */

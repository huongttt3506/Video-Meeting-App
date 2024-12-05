package com.example.video_meeting_app.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WebSocketSessionService {
    private final Map<String, UserSessionInfo> sessionMap = new ConcurrentHashMap<>();

    //Register session with username and roomId
    public void registerSession(String sessionId, String username, String roomId){
        sessionMap.put(sessionId, new UserSessionInfo(username, roomId));
        log.info("Session registered for user: {} in room {}", username, roomId);
    }

    //Retrieve session info by sessionId
    public UserSessionInfo getSessionInfo(String sessionId) {
        return sessionMap.get(sessionId);
    }

    //Remove session when user disconnects
    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
        log.info("Session removed for sessionId: {}", sessionId);
    }

}

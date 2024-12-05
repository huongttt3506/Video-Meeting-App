package com.example.video_meeting_app.websocket;

import lombok.Getter;

@Getter
public class UserSessionInfo {
    private final String username;
    private final String roomId;

    public UserSessionInfo(String username, String roomId){
        this.username = username;
        this.roomId = roomId;
    }

}

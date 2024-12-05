package com.example.video_meeting_app.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
    Manage room members in the server's memory (ConcurrentHashMap)
     */
@Service
@Slf4j
public class RoomSessionService {
    private ConcurrentHashMap<String, List<String>> room;
    public RoomSessionService() {
        this.room = new ConcurrentHashMap<>();
    }

    public List<String> getParticipants(String roomId) {
        return room.getOrDefault(roomId, new ArrayList<>());
    }

    public void removeParticipant(String roomId, String participant) {
        List<String> participants = room.get(roomId);
        if (participants != null) {
            participants.remove(participant);
        }
    }

}

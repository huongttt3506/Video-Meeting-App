package com.example.video_meeting_app.message.dto;

import com.example.video_meeting_app.message.entity.MessageEntity;
import lombok.Data;

@Data
public class MessageNotificationDto {
    private Long roomId;
    private String roomName;
    private Long userId;
    private String username;
    private String message;

    public static MessageNotificationDto fromEntity(MessageEntity message) {
        MessageNotificationDto dto = new MessageNotificationDto();
        dto.setRoomId(message.getRoom().getId());
        dto.setRoomName(message.getRoom().getName());
        dto.setUserId(message.getUser().getId());
        dto.setUsername(message.getUser().getUsername());
        dto.setMessage(message.getUser().getUsername() + " has sent a new message in the room " + message.getRoom().getName());
        return dto;
    }
}

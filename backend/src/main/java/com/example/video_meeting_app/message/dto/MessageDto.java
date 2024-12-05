package com.example.video_meeting_app.message.dto;

import com.example.video_meeting_app.message.entity.FileEntity;
import com.example.video_meeting_app.message.entity.MessageEntity;
import com.example.video_meeting_app.message.enums.MessageType;
import lombok.Data;


@Data
public class MessageDto {
    private Long roomId;
    private String roomName;
    private Long userId;
    private String username;
    private String content;
    private boolean isEdited;
    private MessageType messageType;
    private FileEntity file;
    public static MessageDto fromEntity(MessageEntity message) {
        MessageDto dto = new MessageDto();
        dto.setRoomId(message.getRoom().getId());
        dto.setRoomName(message.getRoom().getName());
        dto.setUserId(message.getUser().getId());
        dto.setUsername(message.getUser().getUsername());
        dto.setContent(message.getContent());
        dto.setEdited(message.isEdited());
        dto.setMessageType(message.getMessageType());
        dto.setFile(message.getFile());
        return dto;
    }
}

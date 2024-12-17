package com.example.video_meeting_app.message.dto;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.message.enums.MessageType;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class SendMessageDto {
    private Long roomId;
    private String content;
    private MessageType messageType;
    private MultipartFile file;


}

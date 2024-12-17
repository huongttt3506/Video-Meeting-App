package com.example.video_meeting_app.message.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditMessageDto {
    private Long messageId;
    private String newContent;
}

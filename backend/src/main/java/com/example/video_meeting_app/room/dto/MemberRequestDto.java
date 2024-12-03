package com.example.video_meeting_app.room.dto;

import com.example.video_meeting_app.room.enums.Authority;
import lombok.Data;

@Data
public class MemberRequestDto {
    private Long userId;
    private Authority authority;
}


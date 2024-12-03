package com.example.video_meeting_app.room.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateRoomRequestDto {
    private String name;
    private String description;
    private boolean isGroup;
    private String imageUrl;
    private List<MemberRequestDto> members;
}

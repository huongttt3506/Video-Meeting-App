package com.example.video_meeting_app.room.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateRoomRequestDto {
    private String name;
    private String description;
    private List<Long> memberIds;
}

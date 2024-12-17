package com.example.video_meeting_app.room.dto;

import com.example.video_meeting_app.room.entity.MemberRoom;
import com.example.video_meeting_app.room.entity.Room;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class RoomDto {
    private Long id;
    private String name;
    private String description;
    private boolean isGroup;
    private String imageUrl;
    private List<MemberRoomDto> members;

    public static RoomDto fromEntity(Room entity) {
        return RoomDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .isGroup(entity.isGroup())
                .imageUrl(entity.getImageUrl())
                .members(entity.getMemberRoomList().stream()
                        .map(MemberRoomDto::fromEntity)
                        .collect(Collectors.toList())
                )
                .build();
    }

}

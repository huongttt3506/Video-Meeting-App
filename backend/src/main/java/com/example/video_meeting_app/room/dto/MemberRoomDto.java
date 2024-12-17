package com.example.video_meeting_app.room.dto;

import com.example.video_meeting_app.room.entity.MemberRoom;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberRoomDto {
    private Long id;
    private Long roomId;
    private Long userId;
    private String username;
    private String authority;
    private Long videoCallId;

    public static MemberRoomDto fromEntity(MemberRoom entity) {
        return MemberRoomDto.builder()
                .id(entity.getId())
                .roomId(entity.getRoom().getId())
                .userId(entity.getMember().getId())
                .authority(String.valueOf(entity.getAuthority()))
                .videoCallId(entity.getVideoCall() != null ? entity.getVideoCall().getId() : null)
                .build();
    }
}

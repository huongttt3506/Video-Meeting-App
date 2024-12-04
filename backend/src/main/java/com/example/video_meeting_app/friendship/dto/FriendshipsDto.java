package com.example.video_meeting_app.friendship.dto;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.friendship.entity.Friendships;
import com.example.video_meeting_app.friendship.enums.FriendshipStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendshipsDto {
    private UserEntity user1;
    private UserEntity user2;
    private FriendshipStatus friendshipStatus;

    public static FriendshipsDto fromEntity(Friendships entity) {
        return FriendshipsDto.builder()
                .user1(entity.getUser1())
                .user2(entity.getUser2())
                .friendshipStatus(entity.getFriendshipStatus())
                .build();
    }


}

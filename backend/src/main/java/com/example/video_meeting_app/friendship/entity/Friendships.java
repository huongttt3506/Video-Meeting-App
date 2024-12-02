package com.example.video_meeting_app.friendship.entity;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.common.entity.BaseEntity;
import com.example.video_meeting_app.friendship.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friendships extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id_1", nullable = false)
    private UserEntity user1; //User sends invitation

    @ManyToOne
    @JoinColumn(name = "user_id_2", nullable = false)
    private UserEntity user2; //The user accepts the invitation

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus friendshipStatus;

}

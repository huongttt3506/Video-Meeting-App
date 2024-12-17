package com.example.video_meeting_app.room.entity;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.room.enums.Authority;
import com.example.video_meeting_app.common.entity.BaseEntity;
import com.example.video_meeting_app.video_call.entity.VideoCalls;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRoom extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority; // HOST, ADMIN, MEMBER

    @ManyToOne
    @JoinColumn(name = "video_call_id")
    private VideoCalls videoCall;


}

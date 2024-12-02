package com.example.video_meeting_app.video_call.entity;

import com.example.video_meeting_app.common.entity.BaseEntity;
import com.example.video_meeting_app.room.entity.MemberRoom;
import com.example.video_meeting_app.room.entity.Rooms;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoCalls extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms room;
    @OneToMany(mappedBy = "videoCall", fetch = FetchType.LAZY)
    private Set<MemberRoom> memberRooms;
}

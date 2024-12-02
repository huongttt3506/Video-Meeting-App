package com.example.video_meeting_app.room.entity;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rooms extends BaseEntity {
    private String name; //room name (In case 1:1 chat, room name is user1_user2)
    private String description; //a description of the group's purpose or other additional information
    private boolean isGroup; // True for group chat, false for 1:1 chat
    private String imageUrl;
    //Relationships to other entities
    //MemberRoom
    @Builder.Default
    @OneToMany(mappedBy = "room")
    private List<MemberRoom> memberRoomList = new ArrayList<>(); //members of room

    public void setTitleForPrivateRoom(UserEntity user1, UserEntity user2) {
        if (!isGroup) {
            this.name = user1.getUsername() + "_" + user2.getUsername();
        }
    }


}

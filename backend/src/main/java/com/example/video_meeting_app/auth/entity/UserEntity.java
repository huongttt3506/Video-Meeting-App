package com.example.video_meeting_app.auth.entity;

import com.example.video_meeting_app.auth.enums.StatusSet;
import com.example.video_meeting_app.auth.enums.UserRole;
import com.example.video_meeting_app.auth.enums.UserStatus;
import com.example.video_meeting_app.common.entity.BaseEntity;
import com.example.video_meeting_app.friendship.entity.Friendships;
import com.example.video_meeting_app.message.entity.Messages;
import com.example.video_meeting_app.room.entity.MemberRoom;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="users")
public class UserEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    private String name;
    private String email;
    private String phone;
    private String profileImg;
    private UserStatus status; //ONLINE, OFFLINE, BUSY
    private UserRole role; //ADMIN, USER
    private StatusSet statusSet; //AUTO, OFFLINE, BUSY

    //Relationships with other entities
//    //Friendships
//    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
//    private List<Friendships> friendshipsAsUser1;
//    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
//    private List<Friendships> friendshipsAsUser2;

    //MemberRooms
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberRoom> memberRooms;

//    //Messages
//    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
//    private List<Messages> messages;
}

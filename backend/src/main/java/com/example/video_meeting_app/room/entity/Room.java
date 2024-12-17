package com.example.video_meeting_app.room.entity;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.common.entity.BaseEntity;
import com.example.video_meeting_app.room.enums.Authority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseEntity {
    @Column(nullable = false)
    private String name; //room name (In case 1:1 chat, room name is user1_user2)
    private String description; //a description of the group's purpose or other additional information
    private boolean isGroup; // True for group chat, false for 1:1 chat
    private String imageUrl;
    //Relationships to other entities
    //MemberRoom
    @Builder.Default
    @OneToMany(mappedBy = "room")
    private List<MemberRoom> memberRoomList = new ArrayList<>(); //members of room

    public boolean isGroupRoom() {
        return this.isGroup;
    }

    public void setNameForPrivateRoom(UserEntity user1, UserEntity user2) {
        if (!isGroup) {
            this.name = user1.getUsername() + "_" + user2.getUsername();
        }
    }

    public List<UserEntity> getMembers() {
        List<UserEntity> members = new ArrayList<>();
        for (MemberRoom memberRoom: memberRoomList) {
            members.add(memberRoom.getMember());
        }
        return members;
    }

    public boolean isOwner(UserEntity user) {
        return memberRoomList.stream()
                .anyMatch(memberRoom -> memberRoom.getMember().equals(user)
                        && memberRoom.getAuthority() == Authority.HOST);
    }

    public boolean isAdmin(UserEntity user) {
        return memberRoomList.stream()
                .anyMatch(memberRoom -> memberRoom.getMember().equals(user)
                        && memberRoom.getAuthority() == Authority.ADMIN
                );
    }

    public void removeMember(UserEntity user) {
        memberRoomList.removeIf(memberRoom -> memberRoom.getMember().equals(user));
    }

    public void addMember(UserEntity user, Authority authority) {
        MemberRoom memberRoom = MemberRoom.builder()
                .room(this)
                .member(user)
                .authority(authority)
                .build();
        this.memberRoomList.add(memberRoom);
    }

    public boolean hasMember(UserEntity user) {
        return memberRoomList.stream()
                .anyMatch(memberRoom -> memberRoom.getMember().equals(user));
    }
}

package com.example.video_meeting_app.auth.dto;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.auth.enums.StatusSet;
import com.example.video_meeting_app.auth.enums.UserRole;
import com.example.video_meeting_app.auth.enums.UserStatus;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String profileImg;
    private UserStatus status;
    private StatusSet statusSet;
    private UserRole role;

    public static UserDto fromEntity(UserEntity entity){
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .profileImg(entity.getProfileImg())
                .status(entity.getStatus())
                .statusSet(entity.getStatusSet())
                .role(entity.getRole())
                .build();
    }





}

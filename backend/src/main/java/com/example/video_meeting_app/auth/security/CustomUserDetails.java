package com.example.video_meeting_app.auth.security;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.auth.enums.UserRole;
import com.example.video_meeting_app.auth.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.DenyAll;
import java.util.Collection;
import java.util.Collections;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String profileImg;
    private UserStatus status;
    private UserRole role;
    @Getter
    private UserEntity entity;

    public static CustomUserDetails fromEntity(UserEntity entity) {
        if (entity == null) throw new IllegalArgumentException("UserEntity cannot be null");
        return CustomUserDetails.builder()
                .entity(entity)
                .build();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (entity == null || entity.getRole() == null) {
            return Collections.emptyList();
        }
        return Collections.singleton(new SimpleGrantedAuthority(entity.getRole().name()));
    }

    @Override
    public String getUsername() {
        return entity != null ? entity.getUsername() : null;
    }

    @Override
    public String getPassword() {
        return entity != null ? entity.getPassword() : null;
    }

    public Long getId() {
        return entity != null ? entity.getId() : null;
    }

    public String getEmail() {
        return entity != null ? entity.getEmail() : null;
    }

    public String getPhone() {
        return entity != null ? entity.getPhone() : null;
    }

    public String getProfileImg() {
        return entity != null ? entity.getProfileImg() : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
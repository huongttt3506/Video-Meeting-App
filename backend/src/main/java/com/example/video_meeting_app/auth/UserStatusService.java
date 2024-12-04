package com.example.video_meeting_app.auth;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.auth.enums.StatusSet;
import com.example.video_meeting_app.auth.enums.UserStatus;
import com.example.video_meeting_app.auth.repository.UserRepository;
import com.example.video_meeting_app.auth.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final AuthenticationFacade authFacade;
    private final UserRepository userRepository;

    // Set status
    public void setUserStatus(UserStatus status) {
        switch (status) {
            case OFFLINE -> updateUserStatus(UserStatus.OFFLINE, StatusSet.OFFLINE);
            case BUSY -> updateUserStatus(UserStatus.BUSY, StatusSet.BUSY);
            case ONLINE -> updateUserStatus(UserStatus.ONLINE, StatusSet.AUTO);
        }
    }

    // Helper method to update user status
    private void updateUserStatus(UserStatus status, StatusSet statusSet) {
        UserEntity user = authFacade.extractUser();
        user.setStatus(status);
        user.setStatusSet(statusSet);
        userRepository.save(user);
    }

    // Get user status
    public UserStatus getUserStatus(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getStatus();
    }

}

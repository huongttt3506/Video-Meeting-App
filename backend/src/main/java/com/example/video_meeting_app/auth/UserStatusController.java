package com.example.video_meeting_app.auth;

import com.example.video_meeting_app.auth.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user-status")
@Slf4j
@RequiredArgsConstructor
public class UserStatusController {
    private final UserStatusService userStatusService;

    @PostMapping("/set-status")
    public ResponseEntity<String> setUserStatus(@RequestParam UserStatus status) {
        try {
            userStatusService.setUserStatus(status);
            return ResponseEntity.ok("User status updated successfully");
        } catch (Exception e) {
            log.error("Error updating user status: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user status");
        }
    }

    @GetMapping("/get-status/{userId}")
    public ResponseEntity<UserStatus> getUserStatus(@PathVariable Long userId) {
        try {
            UserStatus status = userStatusService.getUserStatus(userId);
            return ResponseEntity.ok(status);
        } catch (RuntimeException e) {
            log.error("Error fetching user status: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

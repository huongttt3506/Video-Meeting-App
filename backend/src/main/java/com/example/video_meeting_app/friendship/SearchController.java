package com.example.video_meeting_app.friendship;

import com.example.video_meeting_app.auth.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<UserEntity>> searchUsers(@RequestParam String keyword) {
        List<UserEntity> users = searchService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }
}

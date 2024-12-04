package com.example.video_meeting_app.friendship;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.friendship.entity.Friendships;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("friends")
@RequiredArgsConstructor
public class FriendshipsController {
    private final FriendshipsService friendshipsService;

    // Add friend
    @PostMapping("add/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable Long friendId) {
        friendshipsService.addFriend(friendId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Friend request sent.");
    }

    // Accept a friend request
    @PostMapping("/{friendRequestId}/accept")
    public ResponseEntity<String> acceptFriend(@PathVariable Long friendRequestId) {
        friendshipsService.acceptFriend(friendRequestId);
        return ResponseEntity.ok("Friend request accepted.");
    }

    // Decline a friend request
    @PostMapping("/{friendRequestId}/decline")
    public ResponseEntity<String> declineFriend(@PathVariable Long friendRequestId) {
        friendshipsService.declineFriend(friendRequestId);
        return ResponseEntity.ok("Friend request declined.");
    }
    // Unfriend
    @DeleteMapping("/{friendId}/unfriend")
    public ResponseEntity<String> unfriend(@PathVariable Long friendId) {
        friendshipsService.unfriend(friendId);
        return ResponseEntity.ok("You have unfriended.");
    }

    // Get friendship status
    @GetMapping("/{friendId}/status")
    public ResponseEntity<String> getFriendshipStatus(@PathVariable Long friendId) {
        String status = friendshipsService.getFriendshipStatus(friendId);
        return ResponseEntity.ok(status);
    }

    // Get list of friends
    @GetMapping("/list")
    public ResponseEntity<List<UserEntity>> getFriendsList() {
        List<UserEntity> friends = friendshipsService.readFriendsList();
        return ResponseEntity.ok(friends);
    }

    // Get sent friend requests
    @GetMapping("/sent-requests")
    public ResponseEntity<List<Friendships>> getSentFriendRequests() {
        List<Friendships> sentRequests = friendshipsService.getSentFriendRequests();
        return ResponseEntity.ok(sentRequests);
    }

    // Get received friend requests
    @GetMapping("/received-requests")
    public ResponseEntity<List<Friendships>> getReceivedFriendRequests() {
        List<Friendships> receivedRequests = friendshipsService.getReceivedFriendRequests();
        return ResponseEntity.ok(receivedRequests);
    }

    // Count friends
    @GetMapping("/count")
    public ResponseEntity<Long> countFriends() {
        Long friendCount = friendshipsService.countFriends();
        return ResponseEntity.ok(friendCount);
    }

    //get online friends list
    @GetMapping("/online")
    public ResponseEntity<List<UserEntity>> getOnlineFriends() {
        return ResponseEntity.ok(friendshipsService.readOnlineFriends());
    }

}

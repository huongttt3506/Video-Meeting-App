package com.example.video_meeting_app.friendship;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.auth.enums.UserRole;
import com.example.video_meeting_app.auth.enums.UserStatus;
import com.example.video_meeting_app.auth.repository.UserRepository;
import com.example.video_meeting_app.auth.security.AuthenticationFacade;
import com.example.video_meeting_app.friendship.entity.Friendships;
import com.example.video_meeting_app.friendship.enums.FriendshipStatus;
import com.example.video_meeting_app.friendship.repository.FriendshipsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendshipsService {
    private final AuthenticationFacade authFacade;
    private final UserRepository userRepository;
    private final FriendshipsRepository friendshipsRepository;

    //add friend
    public void addFriend(Long friendId) {
        //check role
        UserEntity user1 = authFacade.extractUser();
        if (user1.getRole().equals(UserRole.ROLE_INACTIVE))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        //find friend user
        UserEntity user2 = userRepository.findById(friendId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (user1.getId().equals(user2.getId())) {
            throw new IllegalStateException();
        }

        //check friendship
        boolean friendshipExists = friendshipsRepository.existsByUser1AndUser2(user1, user2)
                || friendshipsRepository.existsByUser1AndUser2(user2, user1);
        if (friendshipExists) {
            throw new IllegalStateException("Friendship already exists!");
        }
        //add friend
        Friendships friendship = Friendships.builder()
                .user1(user1)
                .user2(user2)
                .friendshipStatus(FriendshipStatus.PENDING)
                .build();
        friendshipsRepository.save(friendship);
    }

    //accept friend
    public void acceptFriend(Long friendRequestId) {
        // current user
        UserEntity currentUser = authFacade.extractUser();

        //find friendship
        Friendships friendship = friendshipsRepository.findById(friendRequestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Check if the current user is an invitation recipient
        if (!friendship.getUser2().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Check friendship status. Only PENDING status can be accepted
        if (!friendship.getFriendshipStatus().equals(FriendshipStatus.PENDING)) {
            throw new IllegalStateException();
        }

        // Set friendship status: ACCEPTED
        friendship.setFriendshipStatus(FriendshipStatus.ACCEPTED);
        friendshipsRepository.save(friendship);
    }

    // decline friend
    public void declineFriend(Long friendRequestId) {
        // Current user
        UserEntity currentUser = authFacade.extractUser();

        // Find friend request
        Friendships friendship = friendshipsRepository.findById(friendRequestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Check if the current user is an invitation recipient
        if (!friendship.getUser2().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Check friendship status. Only PENDING status can be declined
        if (!friendship.getFriendshipStatus().equals(FriendshipStatus.PENDING)) {
            throw new IllegalStateException();
        }
        // Set friendship status to DECLINED
        friendship.setFriendshipStatus(FriendshipStatus.DECLINED);
        friendshipsRepository.save(friendship); // Update the database
    }

    // unfriend
    public void unfriend(Long friendId) {
        // Get current user
        UserEntity currentUser = authFacade.extractUser();
        if (currentUser.getRole().equals(UserRole.ROLE_INACTIVE))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        //find friend user
        UserEntity friendUser = userRepository.findById(friendId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Find the actual friendship object (bidirectional check)
        Friendships friendship = friendshipsRepository.findByUser1AndUser2OrUser2AndUser1(currentUser, friendUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friendship does not exist."));

        // Ensure the friendship is in the ACCEPTED state
        if (!friendship.getFriendshipStatus().equals(FriendshipStatus.ACCEPTED)) {
            throw new IllegalStateException("Cannot unfriend unless the friendship is in the ACCEPTED state.");
        }

        // Delete the friendship
        friendshipsRepository.delete(friendship);
    }

    // read list friends
    public List<UserEntity> readFriendsList() {
        UserEntity currentUser = authFacade.extractUser();
        // Get list friends
        List<Friendships> friendships = friendshipsRepository.findAllByUser1OrUser2AndStatus(currentUser, FriendshipStatus.ACCEPTED);

        return friendships.stream()
                .map(friendship -> friendship.getUser1().equals(currentUser) ? friendship.getUser2() : friendship.getUser1())
                .collect(Collectors.toList());
    }
    //Get Friend Request List
    //Get the list of friend requests the user has sent
    public List<Friendships> getSentFriendRequests() {
        UserEntity currentUser = authFacade.extractUser();
        return friendshipsRepository.findAllByUser1AndFriendshipStatus(currentUser, FriendshipStatus.PENDING);
    }

    // Get a list of friend requests the user has received
    public List<Friendships> getReceivedFriendRequests() {
        UserEntity currentUser = authFacade.extractUser();
        return friendshipsRepository.findAllByUser2AndFriendshipStatus(currentUser, FriendshipStatus.PENDING);
    }

    // Get friendship status
    public String getFriendshipStatus(Long friendId) {
        UserEntity currentUser = authFacade.extractUser();

        UserEntity friendUser = userRepository.findById(friendId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Find the friendship relationship between currentUser and friendUser
        Friendships friendship = friendshipsRepository.findByUser1AndUser2OrUser2AndUser1(currentUser, friendUser)
                .orElse(null);

        if (friendship == null) {
            return "Friend request not sent"; // No friendship exists yet
        }

        // Check the status of the friendship relationship
        switch (friendship.getFriendshipStatus()) {
            case PENDING:
                // If the current user is the one who sent the friend request
                if (friendship.getUser1().equals(currentUser)) {
                    return "Pending approval"; // The current user sent the request
                } else {
                    return "Friend request sent"; // The other user sent the request
                }
            case ACCEPTED:
                return "Friends"; // The friendship has been accepted
            default:
                return "Status undefined"; // Friendship status is undefined
        }
    }

    //count friends
    public Long countFriends() {
        UserEntity currentUser = authFacade.extractUser();
        return friendshipsRepository.countByUser1OrUser2AndStatus(currentUser, FriendshipStatus.ACCEPTED);
    }

    // get list friend is online now
    public List<UserEntity> readOnlineFriends() {
        UserEntity currentUser = authFacade.extractUser();

        List<Friendships> friendships = friendshipsRepository.findAllByUser1OrUser2AndStatus(currentUser, FriendshipStatus.ACCEPTED);

        return friendships.stream()
                .map(friendship -> friendship.getUser1().equals(currentUser) ? friendship.getUser2() : friendship.getUser1())
                .filter(friend -> friend.getStatus().equals(UserStatus.ONLINE))
                .collect(Collectors.toList());
    }

}









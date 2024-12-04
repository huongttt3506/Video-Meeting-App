package com.example.video_meeting_app.friendship.repository;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.friendship.entity.Friendships;
import com.example.video_meeting_app.friendship.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipsRepository extends JpaRepository<Friendships, Long> {
    boolean existsByUser1AndUser2(UserEntity user1, UserEntity user2);
    Optional<Friendships> findByUser1AndUser2OrUser2AndUser1(UserEntity user1, UserEntity user2);

    List<Friendships> findAllByUser1OrUser2AndStatus(UserEntity currentUser, FriendshipStatus friendshipStatus);

    List<Friendships> findAllByUser2AndStatus(UserEntity user, FriendshipStatus friendshipStatus);

    Long countByUser1OrUser2AndStatus(UserEntity currentUser, FriendshipStatus friendshipStatus);

    List<Friendships> findAllByUser1AndFriendshipStatus(UserEntity user, FriendshipStatus friendshipStatus);
    List<Friendships> findAllByUser2AndFriendshipStatus(UserEntity user, FriendshipStatus friendshipStatus);
}

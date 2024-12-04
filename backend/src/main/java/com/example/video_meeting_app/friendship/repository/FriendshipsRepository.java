package com.example.video_meeting_app.friendship.repository;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.friendship.entity.Friendships;
import com.example.video_meeting_app.friendship.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipsRepository extends JpaRepository<Friendships, Long> {
    boolean existsByUser1AndUser2(UserEntity user1, UserEntity user2);

    @Query("SELECT f FROM Friendships f WHERE (f.user1 = :user1 AND f.user2 = :user2) OR (f.user1 = :user2 AND f.user2 = :user1)")
    Optional<Friendships> findByUser1AndUser2OrUser2AndUser1(@Param("user1") UserEntity user1, @Param("user2") UserEntity user2);

    @Query("SELECT f FROM Friendships f WHERE (f.user1 = :user OR f.user2 = :user) AND f.friendshipStatus = :friendshipStatus")
    List<Friendships> findFriendshipsByUserAndStatus(@Param("user") UserEntity user, FriendshipStatus friendshipStatus);

    @Query("SELECT COUNT(f) FROM Friendships f WHERE (f.user1 = :user OR f.user2 = :user) AND f.friendshipStatus = :friendshipStatus")
    Long countFriendshipsByUserAndStatus(@Param("user") UserEntity user, @Param("friendshipStatus") FriendshipStatus friendshipStatus);

    List<Friendships> findAllByUser1AndFriendshipStatus(UserEntity user, FriendshipStatus friendshipStatus);
    List<Friendships> findAllByUser2AndFriendshipStatus(UserEntity user, FriendshipStatus friendshipStatus);
}

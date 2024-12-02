package com.example.video_meeting_app.friendship.repository;

import com.example.video_meeting_app.friendship.entity.Friendships;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipsRepository extends JpaRepository<Friendships, Long> {
}

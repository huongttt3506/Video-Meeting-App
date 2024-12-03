package com.example.video_meeting_app.video_call.repository;

import com.example.video_meeting_app.video_call.entity.VideoCalls;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoCallsRepository extends JpaRepository<VideoCalls, Long> {
}

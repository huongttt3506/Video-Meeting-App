package com.example.video_meeting_app.message.repository;

import com.example.video_meeting_app.message.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Messages, Long> {
}

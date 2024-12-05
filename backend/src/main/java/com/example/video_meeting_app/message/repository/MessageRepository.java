package com.example.video_meeting_app.message.repository;

import com.example.video_meeting_app.message.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}

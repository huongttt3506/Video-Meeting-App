package com.example.video_meeting_app.message.repository;

import com.example.video_meeting_app.message.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("SELECT m FROM MessageEntity m " +
            "WHERE m.room.id = :roomId " +
            "ORDER BY m.createdDate ASC")
    List<MessageEntity> findByRoomId(@Param("roomId") Long roomId);
}

package com.example.video_meeting_app.room.repository;

import com.example.video_meeting_app.room.entity.MemberRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoomRepository extends JpaRepository<MemberRoom, Long> {
}

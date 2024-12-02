package com.example.video_meeting_app.room.repository;

import com.example.video_meeting_app.room.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
}

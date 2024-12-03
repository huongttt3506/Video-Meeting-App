package com.example.video_meeting_app.room.repository;

import com.example.video_meeting_app.room.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    @Query("SELECT r FROM Rooms r JOIN r.memberRoomList m WHERE m.member.id = :userId")
    List<Rooms> findAllByMemberUserId(@Param("userId") Long userId);
}

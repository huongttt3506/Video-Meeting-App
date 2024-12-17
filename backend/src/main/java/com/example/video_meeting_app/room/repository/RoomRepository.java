package com.example.video_meeting_app.room.repository;

import com.example.video_meeting_app.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Rooms r JOIN r.memberRoomList m WHERE m.member.id = :userId")
    List<Room> findAllByMemberUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Room r " +
            "JOIN r.memberRoomList mr1 " +
            "JOIN r.memberRoomList mr2 " +
            "WHERE r.isGroup = false " +
            "AND mr1.member.id IN (:user1Id, :user2Id) " +
            "AND mr2.member.id IN (:user1Id, :user2Id) " +
            "AND mr1.member.id <> mr2.member.id")
    Optional<Room> findOneToOneRoom(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Query("SELECT CASE WHEN COUNT(mr) > 0 THEN true ELSE false END " +
            "FROM MemberRoom mr " +
            "WHERE mr.room.id = :roomId AND mr.member.id = :userId")
    boolean isMemberOfRoom(@Param("roomId") Long roomId, @Param("userId") Long userId);

}

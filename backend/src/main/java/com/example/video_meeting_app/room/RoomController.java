package com.example.video_meeting_app.room;

import com.amazonaws.Response;
import com.example.video_meeting_app.room.dto.CreateRoomRequestDto;
import com.example.video_meeting_app.room.dto.JoinRoomRequestDto;
import com.example.video_meeting_app.room.entity.Rooms;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    // CREATE ROOM
    @PostMapping
    public ResponseEntity<Rooms> createRoom(
            @RequestBody
            CreateRoomRequestDto request
    ) {
        Rooms room = roomService.createRoom(
                request.getName(),
                request.getDescription(),
                request.isGroup(),
                request.getImageUrl(),
                request.getMembers()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    // JOIN ROOM
    @PostMapping("/{roomId}/join")
    public ResponseEntity<String> joinRoom(
            @PathVariable
            Long roomId,
            @RequestBody
            JoinRoomRequestDto request
    ) {
        roomService.joinRoom(roomId, request.getUserId(), request.getAuthority());
        return ResponseEntity.ok("User join the room successfully");
    }

    // Lấy danh sách Room của người dùng
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rooms>> getUserRooms(@PathVariable Long userId) {
        List<Rooms> rooms = roomService.getRoomsForUser(userId);
        return ResponseEntity.ok(rooms);
    }
}

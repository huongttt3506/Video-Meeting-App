package com.example.video_meeting_app.room;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.auth.repository.UserRepository;
import com.example.video_meeting_app.room.dto.MemberRequestDto;
import com.example.video_meeting_app.room.entity.Rooms;
import com.example.video_meeting_app.room.enums.Authority;
import com.example.video_meeting_app.room.repository.RoomsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Member;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomsRepository roomsRepository;
    private final UserRepository userRepository;

    // CREATE ROOM
    public Rooms createRoom(String name, String description, boolean isGroup, String imageUrl, List<MemberRequestDto> members) {
        Rooms room = Rooms.builder()
                .name(name)
                .description(description)
                .isGroup(isGroup)
                .imageUrl(imageUrl)
                .build();
        for (MemberRequestDto memberRequestDto : members) {
            UserEntity user = userRepository.findById(memberRequestDto.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            room.addMember(user, memberRequestDto.getAuthority());
        }
        // nếu như là room 1:1
        if (!isGroup && members.size() == 2) {
            UserEntity user1 = userRepository.findById(members.get(0).getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "User with ID " + members.get(0).getUserId() + " not found"));

            UserEntity user2 = userRepository.findById(members.get(1).getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "User with ID " + members.get(1).getUserId() + " not found"));
            room.setTitleForPrivateRoom(user1, user2);
        }
        return roomsRepository.save(room);
    }

    // JOIN ROOM
    public void joinRoom(Long roomId, Long userId, Authority authority) {
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room ID not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found"));

        if (!room.hasMember(user)) {
            room.addMember(user, authority);
            roomsRepository.save(room);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member already in room");
        }
    }
    // Lấy danh sách Room của người dùng
    public List<Rooms> getRoomsForUser(Long userId) {
        return roomsRepository.findAllByMemberUserId(userId);
    }
}

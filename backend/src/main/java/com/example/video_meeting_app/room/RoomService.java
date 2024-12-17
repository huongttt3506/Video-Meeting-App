package com.example.video_meeting_app.room;

import com.example.video_meeting_app.auth.dto.UserDto;
import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.auth.repository.UserRepository;
import com.example.video_meeting_app.auth.security.AuthenticationFacade;
import com.example.video_meeting_app.room.dto.CreateRoomRequestDto;
import com.example.video_meeting_app.room.dto.RoomDto;
import com.example.video_meeting_app.room.entity.MemberRoom;
import com.example.video_meeting_app.room.entity.Room;
import com.example.video_meeting_app.room.enums.Authority;
import com.example.video_meeting_app.room.repository.MemberRoomRepository;
import com.example.video_meeting_app.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final AuthenticationFacade authFacade;
    private final MemberRoomRepository memberRoomRepository;
    private final RoomRepository roomsRepository;
    private final UserRepository userRepository;

    // CREATE ROOM
    // 1:1 ROOM CHAT
    public RoomDto createOneToOneChatRoom(Long userId) {
        UserEntity user1 = authFacade.extractUser();
        UserEntity user2 = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //Check if room 1:1 between user1 and user 2 was existed
        Optional<Room> existingRoom = roomsRepository.findOneToOneRoom(user1.getId(), user2.getId());
        if (existingRoom.isPresent()) {
            return RoomDto.fromEntity(existingRoom.get());
        }
        //create new room
        Room room = Room.builder()
                .isGroup(false)
                .build();

        //set name base on username of user1 and user 2
        room.setNameForPrivateRoom(user1, user2);

        //save to database
        room = roomsRepository.save(room);

        //create memberRoom then add user1 and user2 to member room list
        MemberRoom member1 = MemberRoom.builder()
                .room(room)
                .member(user1)
                .authority(Authority.MEMBER)
                .build();
        MemberRoom member2 = MemberRoom.builder()
                .room(room)
                .member(user2)
                .authority(Authority.MEMBER)
                .build();
        memberRoomRepository.saveAll(List.of(member1, member2));

        return RoomDto.fromEntity(room);
    }

    // CREATE GROUP CHAT ROOM
    public RoomDto createRoom(CreateRoomRequestDto dto) {
        // User sent request to create room
        UserEntity host = authFacade.extractUser();
        //create room
        Room room = Room.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .isGroup(true)
                .build();
        room = roomsRepository.save(room);
        // add member with HOST authority
        MemberRoom member = MemberRoom.builder()
                .room(room)
                .member(host)
                .authority(Authority.HOST)
                .build();
        memberRoomRepository.save(member);

        return RoomDto.fromEntity(room);
    }

    // ADMIN or HOST add members
    public void addMemberDirectly(Long roomId, Long userId) {
        UserEntity currentUser = authFacade.extractUser();

        // find room
        Room room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //find member
        UserEntity member = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //check authority of current user. Only ADMIN or HOST can add new member
        if (!(room.isOwner(currentUser) || room.isAdmin(currentUser))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // check user was already a member of room or not
        if (room.hasMember(member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member already in room");
        }

        // add new member
        room.addMember(member, Authority.MEMBER);
        roomsRepository.save(room);
    }

    // ADMIN or HOST remove a member
    public void removeMember(Long roomId, Long userId) {
        UserEntity currentUser = authFacade.extractUser();

        // Find room
        Room room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Find member need to be removed
        UserEntity member = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Check authority of  currentUser
        if (!(room.isOwner(currentUser) || room.isAdmin(currentUser))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Check if the member to be deleted is a member of the room
        if (!room.hasMember(member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // Do not allow current users to delete themselves
        if (currentUser.equals(member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // Xóa thành viên
        room.removeMember(member);
        roomsRepository.save(room);
    }

    //MEMBER LEAVE ROOM
    public void leaveRoom(Long roomId) {
        UserEntity currentUser = authFacade.extractUser();

        //find room
        Room room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Check if user is a member of room or not
        if (!room.hasMember(currentUser)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // check if current user is HOST
        if (room.isOwner(currentUser)) {
            // get admin list of room
            List<MemberRoom> admins = room.getMemberRoomList().stream()
                    .filter(memberRoom -> memberRoom.getAuthority() == Authority.ADMIN)
                    .toList();
            if (!admins.isEmpty()) {
                // transfer authority of ADMIN to HOST
                MemberRoom newHost = admins.get(0);
                newHost.setAuthority(Authority.HOST);
            } else {
                // if room do not have any admin, close room
                roomsRepository.delete(room);
            }
        }
        // remove member
        room.removeMember(currentUser);

        // save
        roomsRepository.save(room);
    }

    // Close the room for HOST
    public void closeRoom(Long roomId) {
        UserEntity currentUser = authFacade.extractUser();

        // Find room
        Room room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Check if current user is HOST
        if (!room.isOwner(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Close the room by deleting it
        roomsRepository.delete(room);
    }

    // Upgrade a MEMBER to ADMIN
    public void upgradeMemberToAdmin(Long roomId, Long userId) {
        UserEntity currentUser = authFacade.extractUser();

        // Find room
        Room room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Find member to upgrade
        UserEntity member = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Check if current user is HOST
        if (!room.isOwner(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Check if the member to be upgraded is a member of the room
        if (!room.hasMember(member)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // Check if the member is already an ADMIN
        if (room.getMemberRoomList().stream().anyMatch(memberRoom -> memberRoom.getMember().equals(member) && memberRoom.getAuthority() == Authority.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // Upgrade the member to ADMIN
        room.getMemberRoomList().stream()
                .filter(memberRoom -> memberRoom.getMember().equals(member))
                .findFirst()
                .ifPresent(memberRoom -> memberRoom.setAuthority(Authority.ADMIN));

        // Save the updated room
        roomsRepository.save(room);
    }

    // Get all members of a room by roomId
    public List<UserDto> getAllMembersOfRoom(Long roomId) {
        // Find room by roomId
        Room room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Convert members to UserDTOs
        return room.getMemberRoomList().stream()
                .map(memberRoom -> UserDto.fromEntity(memberRoom.getMember()))
                .collect(Collectors.toList());
    }

    // Get room details by roomId and return as RoomDTO
    public RoomDto getRoomById(Long roomId) {
        Room room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return RoomDto.fromEntity(room);
    }

    // Get all rooms the user has joined and return as RoomDTOs
    public List<RoomDto> getAllRoomsByUser() {
        UserEntity currentUser = authFacade.extractUser();

        List<Room> rooms = roomsRepository.findAll().stream()
                .filter(room -> room.hasMember(currentUser))
                .toList();

        return rooms.stream()
                .map(RoomDto::fromEntity)
                .collect(Collectors.toList());
    }

    //UPDATE ROOM INFO

    //UPDATE ROOM IMAGE







}

















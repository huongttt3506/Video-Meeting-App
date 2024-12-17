package com.example.video_meeting_app.message;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.auth.repository.UserRepository;
import com.example.video_meeting_app.auth.security.AuthenticationFacade;
import com.example.video_meeting_app.common.utils.FileHandlerUtils;
import com.example.video_meeting_app.message.dto.EditMessageDto;
import com.example.video_meeting_app.message.dto.MessageDto;
import com.example.video_meeting_app.message.dto.SendMessageDto;
import com.example.video_meeting_app.message.entity.FileEntity;
import com.example.video_meeting_app.message.entity.MessageEntity;
import com.example.video_meeting_app.message.enums.MessageType;
import com.example.video_meeting_app.message.repository.FileRepository;
import com.example.video_meeting_app.message.repository.MessageRepository;
import com.example.video_meeting_app.room.entity.Room;
import com.example.video_meeting_app.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private AuthenticationFacade authFacade;
    private MessageRepository messageRepository;
    private RoomRepository roomsRepository;
    private UserRepository userRepository;
    private FileHandlerUtils fileHandlerUtils;
    private FileRepository fileRepository;

    public void checkUserMembership(Long roomId, Long userId) {
        boolean isMember = roomsRepository.isMemberOfRoom(roomId, userId);
        if (!isMember) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this room.");
        }
    }

    // Get all messages
    public List<MessageDto> getMessagesByRoom(Long roomId) {
        UserEntity currentUser = authFacade.extractUser();
        Room room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        //check user is member of room or not.
        checkUserMembership(roomId, currentUser.getId());
        //get messages
        return messageRepository.findByRoomId(roomId)
                .stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    // Send new message
    public MessageDto sendMessage(SendMessageDto dto) {
        Room room = roomsRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserEntity user = authFacade.extractUser();

        //check user is member of room or not.
        checkUserMembership(room.getId(), user.getId());

        FileEntity fileEntity = null;
        //check message type
        if (dto.getMessageType() == MessageType.FILE) {
            if (dto.getFile() == null || dto.getFile().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required for FILE message type.");
            }
            String relativePath = fileHandlerUtils.saveFile(
                "messages/",
                UUID.randomUUID().toString(),
                dto.getFile()
            );
            //create FileEntity and save to database
            fileEntity = FileEntity.builder()
                    .fileName(dto.getFile().getOriginalFilename())
                    .filePath(relativePath)
                    .fileSize(dto.getFile().getSize())
                    .fileType(dto.getFile().getContentType())
                    .build();
            fileEntity = fileRepository.save(fileEntity);
        } else if (dto.getMessageType() == MessageType.TEXT && dto.getContent().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content is required for TEXT message type.");
            
        }

        //Create MessageEntity
        MessageEntity message = MessageEntity.builder()
                .room(room)
                .user(user)
                .content(dto.getContent())
                .isEdited(false)
                .messageType(dto.getMessageType())
                .file(fileEntity)
                .build();
        message = messageRepository.save(message);
        return MessageDto.fromEntity(message);
    }

    // update message
    public MessageDto editMessage(EditMessageDto dto) {
        UserEntity currentUser = authFacade.extractUser();
        MessageEntity message = messageRepository.findById(dto.getMessageId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!message.getUser().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        message.setContent(dto.getNewContent());
        message.setEdited(true);
        message = messageRepository.save(message);
        return MessageDto.fromEntity(message);
    }

    //delete message
    public void deleteMessage(Long messageId) {
        UserEntity currentUser = authFacade.extractUser();

        //check if message was sent by current user
        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!message.getUser().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        //check file is existing or not
        if (message.getFile() != null) {
            //delete file
            fileHandlerUtils.deleteFile(message.getFile().getFilePath());
            fileRepository.delete(message.getFile());
        }
        // delete message
        messageRepository.delete(message);
    }

    //send system message when one user leave or join room
    public void sendSystemMessage(Room room, String content, MessageType messageType) {
        MessageEntity message = MessageEntity.builder()
                .room(room)
                .user(null)
                .content(content)
                .isEdited(false)
                .messageType(messageType)
                .file(null)
                .build();
        messageRepository.save(message);
    }
}

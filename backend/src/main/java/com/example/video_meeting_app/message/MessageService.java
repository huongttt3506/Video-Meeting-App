package com.example.video_meeting_app.message;

import com.example.video_meeting_app.auth.repository.UserRepository;
import com.example.video_meeting_app.auth.security.AuthenticationFacade;
import com.example.video_meeting_app.message.repository.MessageRepository;
import com.example.video_meeting_app.room.repository.RoomsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private AuthenticationFacade authFacade;
    private MessageRepository messageRepository;
    private RoomsRepository roomsRepository;
    private UserRepository userRepository;

}

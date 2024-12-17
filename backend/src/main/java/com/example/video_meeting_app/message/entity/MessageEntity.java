package com.example.video_meeting_app.message.entity;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.common.entity.BaseEntity;
import com.example.video_meeting_app.message.enums.MessageType;
import com.example.video_meeting_app.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Message")
public class MessageEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Lob
    @Column(nullable = true)
    private String content;

    private boolean isEdited;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType; //TEXT, FILE, JOIN, LEAVE

    @OneToOne
    @JoinColumn(name = "file_id", nullable = true)
    private FileEntity file;
}

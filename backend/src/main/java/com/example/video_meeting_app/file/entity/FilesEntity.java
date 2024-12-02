package com.example.video_meeting_app.file.entity;

import com.example.video_meeting_app.common.entity.BaseEntity;
import com.example.video_meeting_app.room.entity.Rooms;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilesEntity extends BaseEntity {
    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;
    @Column(nullable = false)

    private long fileSize;

    @Column(nullable = false)
    private String fileType;
}

package com.example.video_meeting_app.message.entity;

import com.example.video_meeting_app.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity extends BaseEntity {
    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;
    @Column(nullable = false)

    private long fileSize;

    @Column(nullable = false)
    private String fileType;
}

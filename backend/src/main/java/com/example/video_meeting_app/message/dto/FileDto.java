package com.example.video_meeting_app.message.dto;

import com.example.video_meeting_app.message.entity.FileEntity;
import lombok.Data;

@Data
public class FileDto {
    private String fileName;
    private String filePath;
    private long fileSize;
    private String fileType;

    public static FileDto fromEntity(FileEntity file) {
        FileDto dto = new FileDto();
        dto.setFileName(file.getFileName());
        dto.setFilePath(file.getFilePath());
        dto.setFileSize(file.getFileSize());
        dto.setFileType(file.getFileType());
        return dto;
    }
}

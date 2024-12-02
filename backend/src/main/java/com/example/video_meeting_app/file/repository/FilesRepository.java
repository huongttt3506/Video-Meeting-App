package com.example.video_meeting_app.file.repository;

import com.example.video_meeting_app.file.entity.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<FilesEntity, Long> {
}

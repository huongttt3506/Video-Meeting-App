package com.example.video_meeting_app.auth.repository;

import com.example.video_meeting_app.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}

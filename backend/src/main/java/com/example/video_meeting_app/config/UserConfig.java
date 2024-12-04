package com.example.video_meeting_app.config;

import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.auth.enums.StatusSet;
import com.example.video_meeting_app.auth.enums.UserRole;
import com.example.video_meeting_app.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserConfig {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Bean
    public CommandLineRunner createUser() {
        return args -> {
            //create admin user
            if (!userRepository.existsByUsername("admin")) {
                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("12345"))
                        .name("admin")
                        .phone("01034567890")
                        .email("admin@example.com")
                        .statusSet(StatusSet.AUTO)
                        .role(UserRole.ROLE_ADMIN)
                        .build();
                userRepository.save(admin);
            }
            //create user test
            if (!userRepository.existsByUsername("huong")) {
                UserEntity user = UserEntity.builder()
                        .username("huong")
                        .password(passwordEncoder.encode("12345"))
                        .name("Huong")
                        .phone("01034567891")
                        .email("huong@example.com")
                        .statusSet(StatusSet.AUTO)
                        .role(UserRole.ROLE_USER)
                        .build();
                userRepository.save(user);
            }
            if (!userRepository.existsByUsername("minh")) {
                UserEntity user = UserEntity.builder()
                        .username("minh")
                        .password(passwordEncoder.encode("12345"))
                        .name("Minh")
                        .phone("01034567892")
                        .email("minh@example.com")
                        .statusSet(StatusSet.AUTO)
                        .role(UserRole.ROLE_USER)
                        .build();
                userRepository.save(user);
            }
            if (!userRepository.existsByUsername("hong")) {
                UserEntity user = UserEntity.builder()
                        .username("hong")
                        .password(passwordEncoder.encode("12345"))
                        .name("Hong")
                        .phone("01034567893")
                        .email("hong@example.com")
                        .statusSet(StatusSet.AUTO)
                        .role(UserRole.ROLE_USER)
                        .build();
                userRepository.save(user);
            }
            if (!userRepository.existsByUsername("hai")) {
                UserEntity user = UserEntity.builder()
                        .username("hai")
                        .password(passwordEncoder.encode("12345"))
                        .name("Hai")
                        .phone("01034567894")
                        .email("hai@example.com")
                        .statusSet(StatusSet.AUTO)
                        .role(UserRole.ROLE_USER)
                        .build();
                userRepository.save(user);
            }
            if (!userRepository.existsByUsername("bao")) {
                UserEntity user = UserEntity.builder()
                        .username("bao")
                        .password(passwordEncoder.encode("12345"))
                        .name("Bao")
                        .phone("01034567895")
                        .email("bao@example.com")
                        .statusSet(StatusSet.AUTO)
                        .role(UserRole.ROLE_USER)
                        .build();
                userRepository.save(user);
            }
        };
    }
}

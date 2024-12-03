package com.example.video_meeting_app.auth;

import com.example.video_meeting_app.auth.dto.CreateUserDto;
import com.example.video_meeting_app.auth.dto.UpdateUserDto;
import com.example.video_meeting_app.auth.dto.UserDto;
import com.example.video_meeting_app.auth.entity.UserEntity;
import com.example.video_meeting_app.auth.enums.UserRole;
import com.example.video_meeting_app.auth.repository.UserRepository;
import com.example.video_meeting_app.auth.security.AuthenticationFacade;
import com.example.video_meeting_app.auth.security.CustomUserDetails;
import com.example.video_meeting_app.auth.security.jwt.JwtTokenUtils;
import com.example.video_meeting_app.auth.security.jwt.dto.JwtRequestDto;
import com.example.video_meeting_app.auth.security.jwt.dto.JwtResponseDto;
import com.example.video_meeting_app.common.utils.FileHandlerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final AuthenticationFacade authFacade;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final FileHandlerUtils fileHandlerUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .map(CustomUserDetails::fromEntity)
                .orElseThrow(() -> new UsernameNotFoundException("not found"));
    }
    @Transactional
    public UserDto createUser(CreateUserDto dto) {
        if (!dto.getPassword().equals(dto.getPasswordCheck()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (userRepo.existsByUsername(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return UserDto.fromEntity(userRepo.save(
                UserEntity.builder()
                        .username(dto.getUsername())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .role(UserRole.ROLE_INACTIVE)
                        .build()));
    }

    public JwtResponseDto signin(JwtRequestDto dto) {
        UserEntity userEntity = userRepo.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(
                dto.getPassword(),
                userEntity.getPassword()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        String jwt = jwtTokenUtils.generateToken(CustomUserDetails.fromEntity(userEntity));
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;
    }
    public UserDto updateUser(UpdateUserDto dto){
        UserEntity userEntity = authFacade.extractUser();
        userEntity.setName(dto.getName());
        userEntity.setPhone(dto.getPhone());
        userEntity.setEmail(dto.getEmail());
        if (
                        userEntity.getName() != null &&
                        userEntity.getEmail() != null &&
                        userEntity.getPhone() != null &&
                        userEntity.getRole().equals(UserRole.ROLE_INACTIVE)
        )
            userEntity.setRole(UserRole.ROLE_USER);
        return UserDto.fromEntity(userRepo.save(userEntity));
    }
    public UserDto profileImg(MultipartFile file) {
        UserEntity userEntity = authFacade.extractUser();
        String requestPath = fileHandlerUtils.saveFile(
                String.format("users/%d/", userEntity.getId()),
                "profile",
                file
        );

        userEntity.setProfileImg(requestPath);
        return UserDto.fromEntity(userRepo.save(userEntity));
    }

    public UserDto getUserInfo() {
        return UserDto.fromEntity(authFacade.extractUser());
    }

}

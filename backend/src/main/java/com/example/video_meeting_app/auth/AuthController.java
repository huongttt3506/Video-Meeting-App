package com.example.video_meeting_app.auth;

import com.example.video_meeting_app.auth.dto.CreateUserDto;
import com.example.video_meeting_app.auth.dto.UpdateUserDto;
import com.example.video_meeting_app.auth.dto.UserDto;
import com.example.video_meeting_app.auth.security.jwt.dto.JwtRequestDto;
import com.example.video_meeting_app.auth.security.jwt.dto.JwtResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService userService;

    @PostMapping("signin")
    public JwtResponseDto signIn(
            @RequestBody
            JwtRequestDto dto
    ) {
        return userService.signin(dto);
    }

    @PostMapping("signup")
    public UserDto signUp(
            @RequestBody
            CreateUserDto dto
    ) {
        return userService.createUser(dto);
    }

    // Logout API
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        userService.logout();
        return ResponseEntity.ok("Logout successful");
    }

    @PutMapping("details")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto signUpFinal(
            @RequestBody
            UpdateUserDto dto
    ) {
        return userService.updateUser(dto);
    }

    @PutMapping(
            value = "profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public UserDto profileImg(
            @RequestParam("file")
            MultipartFile file
    ) {
        return userService.profileImg(file);
    }

    @GetMapping("get-user-info")
    public UserDto getUserInfo() {
        return userService.getUserInfo();
    }

}

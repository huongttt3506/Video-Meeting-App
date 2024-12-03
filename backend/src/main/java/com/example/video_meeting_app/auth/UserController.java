package com.example.video_meeting_app.auth;

import com.example.video_meeting_app.auth.dto.CreateUserDto;
import com.example.video_meeting_app.auth.dto.UpdateUserDto;
import com.example.video_meeting_app.auth.dto.UserDto;
import com.example.video_meeting_app.auth.security.jwt.dto.JwtRequestDto;
import com.example.video_meeting_app.auth.security.jwt.dto.JwtResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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

    @PutMapping("details")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto signUpFinal(
            @RequestBody
            UpdateUserDto dto
    ) {
        return userService.updateUser(dto);
    }

}

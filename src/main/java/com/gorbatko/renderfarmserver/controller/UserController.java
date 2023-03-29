package com.gorbatko.renderfarmserver.controller;

import com.gorbatko.renderfarmserver.dto.UserDto;
import com.gorbatko.renderfarmserver.dto.UserServiceResponseDto;
import com.gorbatko.renderfarmserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gorbatko.renderfarmserver.util.Endpoints.LOGIN_PATH;
import static com.gorbatko.renderfarmserver.util.Endpoints.USER_PATH;

@RestController
@RequestMapping(USER_PATH)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserServiceResponseDto> registerUser(@RequestBody final UserDto dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }

    @PostMapping(LOGIN_PATH)
    public ResponseEntity<UserServiceResponseDto> loginUser(@RequestBody final UserDto dto) {
        return ResponseEntity.ok(userService.loginUser(dto));
    }
}

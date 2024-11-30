package com.bookin.bookin.domain.user.controller;

import com.bookin.bookin.domain.user.dto.UserRequestDTO;
import com.bookin.bookin.domain.user.dto.UserResponseDTO;
import com.bookin.bookin.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO.JoinResultDto> signUp(@RequestBody @Valid UserRequestDTO.JoinDto joinDto) {
        UserResponseDTO.JoinResultDto result = userService.signUp(joinDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

}



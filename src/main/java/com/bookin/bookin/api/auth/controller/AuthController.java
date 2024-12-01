package com.bookin.bookin.api.auth.controller;

import com.bookin.bookin.api.auth.dto.LoginRequestDTO;
import com.bookin.bookin.api.auth.dto.LoginResponseDTO;
import com.bookin.bookin.api.auth.service.AuthService;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDto) {
        ApiResponse<LoginResponseDTO> response = authService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }
}

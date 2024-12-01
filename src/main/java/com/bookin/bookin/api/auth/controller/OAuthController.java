package com.bookin.bookin.api.auth.controller;

import com.bookin.bookin.global.apiPayload.ApiResponse;
import com.bookin.bookin.api.auth.dto.LoginResponseDTO;
import com.bookin.bookin.api.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/authorize")
    public String kakaoConnect() {
        return oAuthService.getAuthorizationUrl();
    }

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> kakaoCallback(@RequestParam String code) {
        return oAuthService.processKakaoCallback(code);
    }
}

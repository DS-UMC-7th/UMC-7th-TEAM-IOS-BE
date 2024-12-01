package com.bookin.bookin.api.auth.service;

import com.bookin.bookin.api.auth.dto.LoginRequestDTO;
import com.bookin.bookin.api.auth.dto.LoginResponseDTO;
import com.bookin.bookin.domain.user.entity.User;
import com.bookin.bookin.domain.user.repository.UserRepository;
import com.bookin.bookin.security.JwtTokenProvider;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<LoginResponseDTO> login(LoginRequestDTO loginRequestDto) {
        // 사용자 ID로 사용자 조회
        User user = userRepository.findByUserId(loginRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(user.getUserId());

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getUserId(), token);
        return ApiResponse.onSuccess(loginResponseDTO);
    }
}

package com.bookin.bookin.domain.user.service;

import com.bookin.bookin.domain.user.dto.UserRequestDTO;
import com.bookin.bookin.domain.user.dto.UserResponseDTO;
import com.bookin.bookin.domain.user.entity.User;
import com.bookin.bookin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO.JoinResultDto signUp(UserRequestDTO.JoinDto joinDto) {

        User user = User.builder()
                .userId(joinDto.getUserId())
                .password(joinDto.getPassword())
                .nickname(joinDto.getNickname())
                .provider(joinDto.getProvider())
                .providerId(0L)  // 기본값 설정 (소셜 로그인이 아니면 0, 소셜 로그인 시 외부 provider ID 설정)
                .email(joinDto.getEmail())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDTO.JoinResultDto.builder()
                .id(savedUser.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

}

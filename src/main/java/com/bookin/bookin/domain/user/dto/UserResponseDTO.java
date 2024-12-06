package com.bookin.bookin.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

public class UserResponseDTO {

    @Getter
    @Builder
    public static class JoinResultDto {

        private final Long id;
        private final LocalDateTime createdAt;

    }
}

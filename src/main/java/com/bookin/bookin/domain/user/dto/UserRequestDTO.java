package com.bookin.bookin.domain.user.dto;

import com.bookin.bookin.domain.user.entity.enums.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    @Setter
    public static class JoinDto {

        @NotBlank(message = "아이디는 필수 입력값입니다.")
        private String userId;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        private String nickname;

        @NotNull(message = "Provider는 필수 입력값입니다.")
        private Provider provider;

        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        private String email;

    }
}

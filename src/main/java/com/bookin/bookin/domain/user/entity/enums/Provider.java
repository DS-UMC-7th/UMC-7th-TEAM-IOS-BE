package com.bookin.bookin.domain.user.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Provider {
    KAKAO("카카오 로그인"),
    BOOKIN("자체 로그인");

    private final String provider;
}

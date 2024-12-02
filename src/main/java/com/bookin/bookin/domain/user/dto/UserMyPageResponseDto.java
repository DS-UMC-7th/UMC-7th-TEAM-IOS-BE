package com.bookin.bookin.domain.user.dto;

import com.bookin.bookin.domain.review.dto.ReviewResponseDTO;

import java.util.List;

public class UserMyPageResponseDto {
    private Long userId;
    private String nickname;
    private String email;
    private String profileImageUrl; // 프로필 이미지 URL 추가
    private List<ReviewResponseDTO> topReviews;

    public UserMyPageResponseDto(Long userId, String nickname, String email, String profileImageUrl, List<ReviewResponseDTO> topReviews) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.topReviews = topReviews;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public List<ReviewResponseDTO> getTopReviews() {
        return topReviews;
    }

    public void setTopReviews(List<ReviewResponseDTO> topReviews) {
        this.topReviews = topReviews;
    }
}

package com.bookin.bookin.domain.user.dto;

import com.bookin.bookin.domain.review.dto.ReviewResponseDTO;
import com.bookin.bookin.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserProfileResponseDTO {
    private Long userId;
    private String nickname;
    private String email;
    private String profileImageUrl; // 프로필 이미지 URL 추가
    private List<ReviewResponseDTO> topReviews;

    public UserProfileResponseDTO(User user, List<ReviewResponseDTO> topReviews) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImageUrl = user.getProfilePicture();
        this.topReviews = topReviews;
    }
}

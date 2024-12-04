package com.bookin.bookin.domain.user.dto;

import com.bookin.bookin.domain.review.dto.ReviewResponseDTO;

import java.util.List;

public class UserProfileResponseDTO {
    private String userName;
    private Long userId;
    private String email;
    private String profilePicture;
    private List<ReviewResponseDTO> topReviews;

    // Constructors, getters, and setters
    public UserProfileResponseDTO(String userName, Long userId, String email, String profilePicture, List<ReviewResponseDTO> topReviews) {
        this.userName = userName;
        this.userId = userId;
        this.email = email;
        this.profilePicture = profilePicture;
        this.topReviews = topReviews;
    }

    // Getters and setters (or use Lombok for @Data)
}


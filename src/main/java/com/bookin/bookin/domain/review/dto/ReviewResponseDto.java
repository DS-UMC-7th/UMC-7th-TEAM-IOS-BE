package com.bookin.bookin.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String content;
    private Float rating;
    private String userNickname;
    private String userId;
    private Long bookId;
    private LocalDateTime createdAt;
    private List<String> tags;
    private List<String> imageUrls;
}

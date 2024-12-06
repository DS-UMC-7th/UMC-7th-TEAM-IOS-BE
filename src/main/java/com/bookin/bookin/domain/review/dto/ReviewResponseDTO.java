package com.bookin.bookin.domain.review.dto;

import com.bookin.bookin.domain.review.entity.Review;
import com.bookin.bookin.domain.review.entity.ReviewImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    private Long id;
    private String content;
    private Float rating;
    private Long userId;
    private Long bookId;
    private List<String> tagNames; // 태그 이름 리스트
    private List<String> imageUrls; // 이미지 URL 리스트
    private LocalDateTime createdAt; // 작성 날짜 추가

    public ReviewResponseDTO(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.userId = review.getUser().getId();
        this.bookId = review.getBook().getId();
        this.tagNames = review.getReviewTags().stream()
                .map(reviewTag -> reviewTag.getTag().getName())
                .collect(Collectors.toList());
        this.imageUrls = review.getReviewImages().stream()
                .map(ReviewImage::getUrl)
                .collect(Collectors.toList());
        this.createdAt = review.getCreatedAt(); // 작성 날짜 매핑
    }
}

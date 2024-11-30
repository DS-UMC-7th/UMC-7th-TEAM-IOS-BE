package com.bookin.bookin.domain.review.dto;

import com.bookin.bookin.domain.review.entity.Review;
import com.bookin.bookin.domain.review.entity.ReviewImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private List<String> tagNames;  // 태그 이름 리스트
    private List<String> imageUrls;  // 이미지 URL 리스트

    public ReviewResponseDTO(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.userId = review.getUser().getId();
        this.bookId = review.getBook().getId();
        // 태그 정보 추가
        this.tagNames = review.getReviewTags().stream()
                .map(reviewTag -> reviewTag.getTag().getName())  // Tag 이름 가져오기
                .collect(Collectors.toList());

        // 이미지 URL 정보 추가
        this.imageUrls = review.getReviewImages().stream()
                .map(ReviewImage::getUrl)  // ReviewImage의 URL 가져오기
                .collect(Collectors.toList());
    }
}

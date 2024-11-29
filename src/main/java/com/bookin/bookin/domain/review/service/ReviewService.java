package com.bookin.bookin.domain.review.service;

import com.bookin.bookin.domain.review.dto.ReviewResponseDto;
import com.bookin.bookin.domain.review.entity.Review;
import com.bookin.bookin.domain.review.entity.ReviewImage;
import com.bookin.bookin.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    // 책 별 리뷰 조회
    public List<ReviewResponseDto> getReviewsByBookId(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        // 엔티티를 dto로 변환
        return reviews.stream()
                .map(review -> new ReviewResponseDto(
                        review.getId(),
                        review.getContent(),
                        review.getRating(),
                        review.getUser().getNickname(),
                        review.getUser().getUserId(),
                        review.getBook().getId(),
                        review.getCreatedAt(),
                        review.getReviewTags().stream()
                                .map(tag -> tag.getTag().getName()) // 태그 이름 추출
                                .collect(Collectors.toList()),
                        review.getReviewImages().stream()
                                .map(ReviewImage::getUrl) // 이미지 url 추출
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}

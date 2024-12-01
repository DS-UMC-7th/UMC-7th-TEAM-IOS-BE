package com.bookin.bookin.domain.review.controller;

import com.bookin.bookin.domain.review.dto.RatingResponseDto;
import com.bookin.bookin.domain.review.dto.BookReviewResponseDto;
import com.bookin.bookin.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReviewController {
    private final ReviewService reviewService;

    // 책 별 리뷰 조회 endpoint
    @GetMapping("/books/{bookId}/reviews")
    public ResponseEntity<List<BookReviewResponseDto>> getReviewByBookId(@PathVariable Long bookId) {
        List<BookReviewResponseDto> reviews = reviewService.getReviewsByBookId(bookId);
        return ResponseEntity.ok(reviews); // 200 ok, 데이터 반환
    }

    // 책 별 별점 조회 endpoint
    @GetMapping("/books/{bookId}/rating")
    public ResponseEntity<RatingResponseDto> getBookRatingStatistics(@PathVariable Long bookId) {
        RatingResponseDto ratingStatistics = reviewService.getRatingStatistics(bookId);
        return ResponseEntity.ok(ratingStatistics); // 200 ok
    }
}

package com.bookin.bookin.domain.review.controller;

import java.util.List;
import com.bookin.bookin.domain.review.dto.RatingResponseDto;
import com.bookin.bookin.domain.review.dto.BookReviewResponseDto;
import com.bookin.bookin.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books/{bookId}")
public class BookReviewController {
    private final ReviewService reviewService;

    // 책 별 리뷰 조회 endpoint
    @Operation(
            summary = "책별 리뷰 조회 API",
            description = "특정 책의 모든 리뷰를 조회합니다.",
            parameters = {
                    @Parameter(name = "bookId", description = "책 ID", required = true, example = "9")
            }
    )
    @GetMapping("/reviews")
    public ResponseEntity<List<BookReviewResponseDto>> getReviewByBookId(@PathVariable Long bookId) {
        List<BookReviewResponseDto> reviews = reviewService.getReviewsByBookId(bookId);
        return ResponseEntity.ok(reviews); // 200 ok, 데이터 반환
    }

    // 책 별 별점 조회 endpoint
    @Operation(
            summary = "책별 평점 통계 조회 API",
            description = "특정 책에 대한 리뷰 평점의 평균과 각 평점 비율을 조회합니다.",
            parameters = {
                    @Parameter(name = "bookId", description = "책 ID", required = true, example = "5")
            }
    )
    @GetMapping("/rating")
    public ResponseEntity<RatingResponseDto> getBookRatingStatistics(@PathVariable Long bookId) {
        RatingResponseDto ratingStatistics = reviewService.getRatingStatistics(bookId);
        return ResponseEntity.ok(ratingStatistics); // 200 ok
    }
}

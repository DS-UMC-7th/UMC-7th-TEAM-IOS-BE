package com.bookin.bookin.domain.review.controller;

import com.bookin.bookin.domain.review.dto.ReviewRequestDTO;
import com.bookin.bookin.domain.review.dto.ReviewResponseDTO;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookin.bookin.domain.review.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성 API", description = "리뷰를 저장하는 API입니다. 책이 존재하지 않을 경우, 책 정보도 함께 저장됩니다.")
    @PostMapping
    public ApiResponse<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO request) {
        return ApiResponse.onSuccess(reviewService.createReview(request));
    }

    @Operation(summary = "리뷰 정렬 API", description = "리뷰를 다양한 기준으로 정렬하여 반환합니다.")
    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getSortedReviews(@RequestParam(defaultValue = "latest") String sortBy) {
        return ResponseEntity.ok(reviewService.getSortedReviews(sortBy));
    }

    @Operation(summary = "특정 리뷰 조회 API", description = "특정 리뷰의 상세 정보를 조회합니다.")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewById(reviewId));
    }

}

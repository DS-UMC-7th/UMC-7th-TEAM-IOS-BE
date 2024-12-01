package com.bookin.bookin.domain.review.controller;

import com.bookin.bookin.domain.review.dto.ReviewRequestDTO;
import com.bookin.bookin.domain.review.dto.ReviewResponseDTO;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bookin.bookin.domain.review.service.ReviewService;

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
}

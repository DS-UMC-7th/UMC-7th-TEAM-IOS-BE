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

    @Operation(summary = "리뷰 작성")
    @PostMapping
    public ApiResponse<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO request) {
        ReviewResponseDTO response = reviewService.createReview(request);
        return ApiResponse.onSuccess(response);
    }
}

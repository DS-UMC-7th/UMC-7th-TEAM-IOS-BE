package com.bookin.bookin.domain.user.controller;

import com.bookin.bookin.domain.review.dto.ReviewResponseDTO;
import com.bookin.bookin.domain.user.dto.UserRequestDTO;
import com.bookin.bookin.domain.user.dto.UserResponseDTO;
import com.bookin.bookin.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO.JoinResultDto> signUp(@RequestBody @Valid UserRequestDTO.JoinDto joinDto) {
        UserResponseDTO.JoinResultDto result = userService.signUp(joinDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "사용자 마이페이지 조회 API", description = "사용자 정보와 상위 3개 리뷰를 조회합니다.")
    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPage(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getMyPage(userId));
    }

    // 변경된 엔드포인트: /auth/reviews
    @Operation(summary = "사용자가 작성한 모든 리뷰 조회 API", description = "사용자가 작성한 모든 리뷰를 조회합니다.")
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> getUserReviews(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getUserReviews(userId));
    }

    // 변경된 엔드포인트: /auth/reviews/sorted
    @Operation(summary = "사용자가 작성한 모든 리뷰 정렬 API", description = "사용자가 작성한 모든 리뷰를 정렬하여 조회합니다.")
    @GetMapping("/reviews/sorted")
    public ResponseEntity<List<ReviewResponseDTO>> getSortedUserReviews(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getSortedUserReviews(userId));
    }

    // 변경된 엔드포인트: /auth/reviews/detail
    @Operation(summary = "리뷰 상세 조회 API", description = "특정 리뷰의 상세 정보를 조회합니다.")
    @GetMapping("/reviews/detail")
    public ResponseEntity<ReviewResponseDTO> getReviewDetail(@RequestParam Long userId, @RequestParam Long reviewId) {
        return ResponseEntity.ok(userService.getReviewDetail(userId, reviewId));
    }
}

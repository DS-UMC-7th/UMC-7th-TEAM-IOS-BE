package com.bookin.bookin.domain.review.dto;

import com.bookin.bookin.domain.book.dto.request.BookRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "리뷰 생성 요청 데이터")
@Getter
@Setter
public class ReviewRequestDTO {
    @NotNull
    private Long userId;
    @NotNull
    private String content;
    @NotNull
    private Float rating;
    @Size(max = 5, message = "이미지는 최대 5개까지 첨부할 수 있습니다.")
    private List<String> imageUrls;  // 리뷰 이미지 URL 목록
    @NotNull
    private List<Long> tagIds;  // 선택한 태그 목록
    @NotNull(message = "책 정보를 추가해주세요.")
    private BookRequestDTO book; // 책 정보
}

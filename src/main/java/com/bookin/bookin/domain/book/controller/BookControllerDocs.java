package com.bookin.bookin.domain.book.controller;

import com.bookin.bookin.domain.book.dto.response.BookDetailResponse;
import com.bookin.bookin.domain.book.dto.response.BookListResponse;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "책 관리", description = "책 관리 API")
public interface BookControllerDocs {
    @Operation(summary = "책 목록 조회 API", description = "설정한 정렬에 따라 책 목록을 조회합니다.")
    public ApiResponse<BookListResponse> getBooks(
            @RequestParam(value = "sortedBy", required = false) @Parameter(description = "정렬순",
                    examples =
                            {@ExampleObject(name = "리뷰 최신순", summary = "최신순 정렬", value = "latest"),
                                    @ExampleObject(name = "별점 높은순", summary = "추천순 정렬", value = "highest"),
                                    @ExampleObject(name = "리뷰 많은순", summary = "인기순 정렬", value = "popular")}) String sortedBy,
             @RequestParam(value = "page", defaultValue = "0") int page,
             @RequestParam(value = "size", defaultValue = "3") int size
    );
    @Operation(summary = "책 상세 조회 API", description = "bookId에 따라 책 상세 정보를 조회합니다.")
    public ApiResponse<BookDetailResponse> getBookDetail(
            @PathVariable(value = "bookId") @Parameter(description = "책 Id") Long bookId);
}

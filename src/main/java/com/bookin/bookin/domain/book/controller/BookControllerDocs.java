package com.bookin.bookin.domain.book.controller;

import com.bookin.bookin.domain.book.dto.response.BookListResponse;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.web.bind.annotation.RequestParam;

public interface BookControllerDocs {
    public ApiResponse<BookListResponse> getBooks(
            @RequestParam(value = "sortedBy", required = false) @Parameter(description = "정렬순",
                    examples =
                            {@ExampleObject(name = "리뷰 최신순", summary = "최신순 정렬", value = "latest"),
                                    @ExampleObject(name = "별점 높은순", summary = "별점 높은순 정렬", value = "highest")}) String sortedBy,
             @RequestParam(value = "page", defaultValue = "0") int page,
             @RequestParam(value = "size", defaultValue = "3") int size
    );
}

package com.bookin.bookin.domain.book.controller;

import com.bookin.bookin.domain.book.dto.response.BookListResponse;
import com.bookin.bookin.domain.book.service.BookService;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController implements BookControllerDocs{
    private final BookService bookService;

    @GetMapping
    public ApiResponse<BookListResponse> getBooks(String sortedBy, int page, int size) {
        return ApiResponse.onSuccess(bookService.getBooks(sortedBy, page, size));
    }
}

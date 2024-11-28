package com.bookin.bookin.domain.book.controller;

import com.bookin.bookin.domain.book.dto.response.BookDTO;
import com.bookin.bookin.domain.book.dto.response.BookListResponse;
import com.bookin.bookin.domain.book.service.BookService;
import com.bookin.bookin.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class BookController implements BookControllerDocs{
    private final BookService bookService;

    @Override
    public ApiResponse<BookListResponse> getBooks(Long userId, String sortedBy, int page, int size) {
        return ApiResponse.onSuccess(bookService.getBooks(sortedBy, page, size));
    }
}

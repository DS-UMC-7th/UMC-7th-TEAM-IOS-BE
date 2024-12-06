package com.bookin.bookin.domain.book.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record BookListResponse (
        boolean isLast,
        int totalPage,
        long totalElement,
        List<BookDTO> books
){
}

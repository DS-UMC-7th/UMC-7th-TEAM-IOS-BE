package com.bookin.bookin.domain.book.dto.response;

import com.bookin.bookin.domain.book.entity.Book;
import lombok.Builder;

@Builder
public record BookInfoDTO(
        String bookImg,
        String title,
        String author,
        String publisher
) {
    public static BookInfoDTO of(Book book) {
        return BookInfoDTO.builder()
                .bookImg(book.getImage())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .build();
    }
}

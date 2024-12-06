package com.bookin.bookin.domain.book.dto.response;

import com.bookin.bookin.domain.book.entity.Book;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BookDetailResponse (
        Long id,
        String title,
        String author,
        String isbn,
        String publisher,
        String description,
        LocalDateTime publishedDate,
        String image,
        Integer price
){
    public static BookDetailResponse of(Book book) {
        return BookDetailResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .description(book.getDescription())
                .publishedDate(book.getPublishedDate())
                .image(book.getImage())
                .price(book.getPrice())
                .build();
    }
}

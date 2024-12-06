package com.bookin.bookin.domain.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BookRequestDTO {
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String description;
    private LocalDateTime publishedDate;
    private String imageUrl;
    private Integer price;
}

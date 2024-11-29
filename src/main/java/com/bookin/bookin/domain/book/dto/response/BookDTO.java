package com.bookin.bookin.domain.book.dto.response;

import com.bookin.bookin.domain.book.entity.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "책 정보 응답 dto")
@Builder
public record BookDTO (
        @Schema(description = "책 id")
        Long id,
        @Schema(description = "책 썸네일")
        String imgUrl,
        @Schema(description = "책 제목")
        String title,
        @Schema(description = "책 저자")
        String author,
        @Schema(description = "책 출판사")
        String publisher,
        @Schema(description = "책 평균 별점")
        Float rating,
        @Schema(description = "책 리뷰 개수")
        Integer reviewCount,
        @Schema(description = "책 설명")
        String description)
{
    public static BookDTO of(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .imgUrl(book.getImage())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .rating(book.getRating())
                .description(book.getDescription())
                .reviewCount(book.getReviews().size())
                .build();
    }
}

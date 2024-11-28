package com.bookin.bookin.domain.book.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "책 정보 응답 dto")
@Getter
@Builder
public class BookDTO {
    @Schema(description = "책 id")
    Long id;

    String imgUrl;
    String title;
    String author;
    String publisher;
    Float rating;
    Integer reviewCount;
    String description;
}

package com.bookin.bookin.domain.review.dto;

import com.bookin.bookin.domain.book.dto.response.BookInfoDTO;

public record MyReviewResponseDTO(
        BookInfoDTO book,
        ReviewResponseDTO review
) {
}

package com.bookin.bookin.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingResponseDto {
    private Long bookId;
    private Float rating;
    private Long reviewCount;
    private Integer star5Rate;
    private Integer star4Rate;
    private Integer star3Rate;
    private Integer star2Rate;
    private Integer star1Rate;
}

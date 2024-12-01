package com.bookin.bookin.domain.review.service;

import com.bookin.bookin.domain.book.entity.Book;
import com.bookin.bookin.domain.book.repository.BookRepository;
import com.bookin.bookin.domain.review.dto.RatingResponseDto;
import com.bookin.bookin.domain.review.dto.BookReviewResponseDto;
import com.bookin.bookin.domain.review.entity.Review;
import com.bookin.bookin.domain.review.entity.ReviewImage;
import com.bookin.bookin.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository BookRepository;

    // 책 별 리뷰 조회
    public List<BookReviewResponseDto> getReviewsByBookId(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        // 엔티티를 dto로 변환
        return reviews.stream()
                .map(review -> new BookReviewResponseDto(
                        review.getId(),
                        review.getContent(),
                        review.getRating(),
                        review.getUser().getNickname(),
                        review.getUser().getUserId(),
                        review.getBook().getId(),
                        review.getCreatedAt(),
                        review.getReviewTags().stream()
                                .map(tag -> tag.getTag().getName()) // 태그 이름 추출
                                .collect(Collectors.toList()),
                        review.getReviewImages().stream()
                                .map(ReviewImage::getUrl) // 이미지 url 추출
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // 책 별 평점 조회
    public RatingResponseDto getRatingStatistics(Long bookId) {
        // 책 확인
        Book book = BookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // 리뷰 목록 가져오기
        List<Review> reviews = reviewRepository.findByBookId(bookId);

        // 리뷰 총 개수
        Long reviewCount = reviewRepository.countByBookId(bookId);

        if (reviewCount == 0) {
            // 리뷰 없는 경우 모두 0으로 반환
            return new RatingResponseDto(
                    bookId,
                    0.0f,
                    0L,
                    0, 0, 0, 0, 0
            );
        }

        // 평균 평점 계산
        Float averageRating = book.getRating();

        // 평점별 개수 계산 (반올림하여 집계)
        long star5Count = reviews.stream().filter(r -> Math.round(r.getRating()) == 5).count();
        long star4Count = reviews.stream().filter(r -> Math.round(r.getRating()) == 4).count();
        long star3Count = reviews.stream().filter(r -> Math.round(r.getRating()) == 3).count();
        long star2Count = reviews.stream().filter(r -> Math.round(r.getRating()) == 2).count();
        long star1Count = reviews.stream().filter(r -> Math.round(r.getRating()) == 1).count();

        // 비율 계산
        int star5Rate = (int) Math.round((star5Count * 100.0) / reviewCount);
        int star4Rate = (int) Math.round((star4Count * 100.0) / reviewCount);
        int star3Rate = (int) Math.round((star3Count * 100.0) / reviewCount);
        int star2Rate = (int) Math.round((star2Count * 100.0) / reviewCount);

        // 마지막 비율 계산 (남는 비율을 추가)
        int currentTotalRate = star5Rate + star4Rate + star3Rate + star2Rate;
        int star1Rate = Math.max(0, 100 - currentTotalRate); // 100에서 현재 합계를 빼고, 음수 방지

        // DTO 반환
        return new RatingResponseDto(
                bookId,
                averageRating,
                reviewCount,
                star5Rate,
                star4Rate,
                star3Rate,
                star2Rate,
                star1Rate
        );
    }
}

package com.bookin.bookin.domain.review.service;

import com.bookin.bookin.domain.book.entity.Book;
import com.bookin.bookin.domain.book.repository.BookRepository;
import com.bookin.bookin.domain.book.service.BookService;
import com.bookin.bookin.domain.review.dto.BookReviewResponseDto;
import com.bookin.bookin.domain.review.dto.RatingResponseDto;
import com.bookin.bookin.domain.review.entity.Review;
import com.bookin.bookin.domain.review.entity.ReviewImage;
import com.bookin.bookin.domain.review.entity.ReviewTag;
import com.bookin.bookin.domain.review.entity.Tag;
import com.bookin.bookin.domain.review.repository.ReviewImageRepository;
import com.bookin.bookin.domain.review.repository.ReviewRepository;
import com.bookin.bookin.domain.review.repository.ReviewTagRepository;
import com.bookin.bookin.domain.review.repository.TagRepository;
import com.bookin.bookin.domain.user.entity.User;
import com.bookin.bookin.domain.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bookin.bookin.domain.review.dto.ReviewRequestDTO;
import com.bookin.bookin.domain.review.dto.ReviewResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewTagRepository reviewTagRepository;
    private final TagRepository tagRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookService bookService;
    private final BookRepository BookRepository;

    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {

        // 로깅 추가
        System.out.println("Received request: " + request.getTagIds());

        // 해당 리뷰의 사용자, 책
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findByIsbn(request.getBook().getIsbn())
                .orElseGet(() -> bookService.saveBook(request.getBook()));

        // 리뷰 엔티티 생성
        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .user(user)
                .book(book)
                .build();

        reviewRepository.save(review);

        // 별점 평균 계산
        updateBookRating(book, review.getRating());

        // 태그 추가
        if (request.getTagIds() == null || request.getTagIds().isEmpty()) {
            throw new IllegalArgumentException("태그는 반드시 하나 이상 지정해야 합니다.");
        }

        for (Long tagId : request.getTagIds()) {
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 태그 ID입니다: " + tagId));
            ReviewTag reviewTag = ReviewTag.builder()
                    .review(review)
                    .tag(tag)
                    .build();
            reviewTagRepository.save(reviewTag);
            review.getReviewTags().add(reviewTag);
        }

        // 이미지 추가
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            for (String imageUrl : request.getImageUrls()) {
                ReviewImage reviewImage = ReviewImage.builder()
                        .url(imageUrl)
                        .review(review)
                        .build();
                reviewImageRepository.save(reviewImage);
                review.getReviewImages().add(reviewImage);
            }
        }

        return new ReviewResponseDTO(review);
    }

    @Transactional
    public void updateBookRating(Book book, Float rating) {
        Float averageRating = (book.getRating() == null)
                ? rating
                : reviewRepository.calculateAverageRatingByBookId(book.getId());

        bookService.updateBookRating(book, averageRating);
    }

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

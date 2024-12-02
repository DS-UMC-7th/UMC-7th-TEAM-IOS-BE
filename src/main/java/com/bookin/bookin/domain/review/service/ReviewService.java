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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // 리뷰를 생성하는 메서드
    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {
        // 요청받은 태그 ID를 로그에 출력
        System.out.println("Received request: " + request.getTagIds());

        // 사용자와 책 정보를 데이터베이스에서 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Book book = bookRepository.findByIsbn(request.getBook().getIsbn())
                .orElseGet(() -> bookService.saveBook(request.getBook()));

        // 새로운 리뷰 엔티티 생성
        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .user(user)
                .book(book)
                .build();

        // 리뷰를 저장
        reviewRepository.save(review);

        // 책 평점 업데이트
        updateBookRating(book, review.getRating());

        // 리뷰에 태그 추가
        if (request.getTagIds() == null || request.getTagIds().isEmpty()) {
            throw new IllegalArgumentException("태그는 반드시 하나 이상 지정해야 합니다.");
        }

        for (Long tagId : request.getTagIds()) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 태그 ID입니다: " + tagId));
            ReviewTag reviewTag = ReviewTag.builder()
                    .review(review)
                    .tag(tag)
                    .build();
            reviewTagRepository.save(reviewTag);
            review.getReviewTags().add(reviewTag);
        }

        // 리뷰에 이미지 추가
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

        // 리뷰 응답 DTO 반환
        return new ReviewResponseDTO(review);
    }

    // 책의 평점을 업데이트하는 메서드
    @Transactional
    public void updateBookRating(Book book, Float rating) {
        Float averageRating = (book.getRating() == null)
                ? rating
                : reviewRepository.calculateAverageRatingByBookId(book.getId());

        bookService.updateBookRating(book, averageRating);
    }

    // 책 ID로 리뷰 조회
    public List<BookReviewResponseDto> getReviewsByBookId(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
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
                                .map(ReviewImage::getUrl) // 이미지 URL 추출
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // 책에 대한 평점 통계 조회
    public RatingResponseDto getRatingStatistics(Long bookId) {
        // 책 존재 여부 확인
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));

        // 해당 책의 리뷰 목록 조회
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        Long reviewCount = reviewRepository.countByBookId(bookId);

        // 리뷰가 없는 경우 기본값 반환
        if (reviewCount == 0) {
            return new RatingResponseDto(
                    bookId,
                    0.0f,
                    0L,
                    0, 0, 0, 0, 0
            );
        }

        // 평균 평점 및 평점별 개수 계산
        Float averageRating = book.getRating();
        long star5Count = reviews.stream().filter(r -> Math.round(r.getRating()) == 5).count();
        long star4Count = reviews.stream().filter(r -> Math.round(r.getRating()) == 4).count();
        long star3Count = reviews.stream().filter(r -> Math.round(r.getRating()) == 3).count();
        long star2Count = reviews.stream().filter(r -> Math.round(r.getRating()) == 2).count();
        long star1Count = reviews.stream().filter(r -> Math.round(r.getRating()) == 1).count();

        // 평점 비율 계산
        int star5Rate = (int) Math.round((star5Count * 100.0) / reviewCount);
        int star4Rate = (int) Math.round((star4Count * 100.0) / reviewCount);
        int star3Rate = (int) Math.round((star3Count * 100.0) / reviewCount);
        int star2Rate = (int) Math.round((star2Count * 100.0) / reviewCount);
        int currentTotalRate = star5Rate + star4Rate + star3Rate + star2Rate;
        int star1Rate = Math.max(0, 100 - currentTotalRate); // 현재 합계를 빼고 음수 방지

        // 통계 DTO 반환
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

    // 사용자별 상위 3개의 리뷰를 조회하는 메서드
    public List<ReviewResponseDTO> getTopReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findTop3ByUserIdOrderByRatingDesc(userId);
        return reviews.stream()
                .map(ReviewResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 사용자의 리뷰를 정렬 기준에 따라 가져오는 메서드
    public List<ReviewResponseDTO> getMyReviews(Long userId, String sort) {
        List<Review> reviews;

        switch (sort) {
            case "high-rating":
                reviews = reviewRepository.findByUserIdOrderByRatingDesc(userId);
                break;
            case "low-rating":
                reviews = reviewRepository.findByUserIdOrderByRatingAsc(userId);
                break;
            case "newest":
                reviews = reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
                break;
            case "oldest":
                reviews = reviewRepository.findByUserIdOrderByCreatedAtAsc(userId);
                break;
            default:
                reviews = reviewRepository.findByUserId(userId);
        }

        return reviews.stream()
                .map(ReviewResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 리뷰 ID로 특정 리뷰를 조회하는 메서드
    public ReviewResponseDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        return new ReviewResponseDTO(review);
    }
}

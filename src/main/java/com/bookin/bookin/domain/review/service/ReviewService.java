package com.bookin.bookin.domain.review.service;

import com.bookin.bookin.domain.book.entity.Book;
import com.bookin.bookin.domain.book.repository.BookRepository;
import com.bookin.bookin.domain.book.service.BookService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bookin.bookin.domain.review.dto.ReviewRequestDTO;
import com.bookin.bookin.domain.review.dto.ReviewResponseDTO;
@Slf4j
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

    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {

        // 로깅 추가
        System.out.println("Received request: " + request.getTagIds());

        // 해당 리뷰의 사용자, 책
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("책의 isbn : {}", request.getBook().getIsbn());
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

        // 별점 평균 계산 및 반영
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
}

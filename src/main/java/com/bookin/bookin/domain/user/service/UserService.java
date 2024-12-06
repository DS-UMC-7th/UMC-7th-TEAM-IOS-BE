package com.bookin.bookin.domain.user.service;

import com.bookin.bookin.domain.book.dto.response.BookInfoDTO;
import com.bookin.bookin.domain.book.entity.Book;
import com.bookin.bookin.domain.review.dto.MyReviewResponseDTO;
import com.bookin.bookin.domain.review.dto.ReviewResponseDTO;
import com.bookin.bookin.domain.review.entity.Review;
import com.bookin.bookin.domain.review.entity.ReviewImage;
import com.bookin.bookin.domain.review.repository.ReviewRepository;
import com.bookin.bookin.domain.user.dto.UserRequestDTO;
import com.bookin.bookin.domain.user.dto.UserResponseDTO;
import com.bookin.bookin.domain.user.entity.User;
import com.bookin.bookin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public UserResponseDTO.JoinResultDto signUp(UserRequestDTO.JoinDto joinDto) {
        User user = User.builder()
                .userId(joinDto.getUserId())
                .password(joinDto.getPassword())
                .nickname(joinDto.getNickname())
                .provider(joinDto.getProvider())
                .providerId(0L)
                .email(joinDto.getEmail())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDTO.JoinResultDto.builder()
                .id(savedUser.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getMyPage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        Map<String, Object> response = new HashMap<>();
        response.put("profilePicture", user.getProfilePicture());
        response.put("userId", user.getId());
        response.put("userName", user.getNickname());
        response.put("email", user.getEmail());

        List<ReviewResponseDTO> topReviews = reviewRepository.findTop3ByUserIdOrderByRatingDesc(userId)
                .stream()
                .map(ReviewResponseDTO::new)
                .collect(Collectors.toList());

        response.put("topReviews", topReviews);
        return response;
    }

    @Transactional(readOnly = true)
    public List<MyReviewResponseDTO> getUserReviews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        List<Review> reviews = reviewRepository.findByUser(user);

        return reviews.stream()
                .map(review -> {
                    // 책 정보 생성
                    Book book = review.getBook();
                    BookInfoDTO bookInfoDTO = BookInfoDTO.of(book);

                    ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO(review);

                    return new MyReviewResponseDTO(bookInfoDTO, reviewResponseDTO);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MyReviewResponseDTO> getSortedUserReviews(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        List<Review> reviews = reviewRepository.findByUser(user);

        return reviews.stream()
                .sorted(Comparator.comparing(Review::getRating).reversed())
                .map(review -> {
                    // 책 정보 생성
                    Book book = review.getBook();
                    BookInfoDTO bookInfoDTO = BookInfoDTO.of(book);

                    ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO(review);

                    return new MyReviewResponseDTO(bookInfoDTO, reviewResponseDTO);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewDetail(Long userId, Long reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("사용자가 작성한 리뷰가 아닙니다.");
        }

        return new ReviewResponseDTO(review);
    }
}

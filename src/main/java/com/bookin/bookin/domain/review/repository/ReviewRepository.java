package com.bookin.bookin.domain.review.repository;

import com.bookin.bookin.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // bookId에 대한 리뷰 list 가져오기
    List<Review> findByBookId(Long bookId);

    // bookId에 대한 총 리뷰수
    Long countByBookId(Long bookId);
}

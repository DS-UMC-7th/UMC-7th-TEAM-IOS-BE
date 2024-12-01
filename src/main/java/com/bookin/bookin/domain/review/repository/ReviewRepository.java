package com.bookin.bookin.domain.review.repository;

import com.bookin.bookin.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 별점 평균 계산
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Float calculateAverageRatingByBookId(@Param("bookId") Long bookId);

    // bookId에 대한 리뷰 list 가져오기
    List<Review> findByBookId(Long bookId);

    // bookId에 대한 총 리뷰수
    Long countByBookId(Long bookId);
}

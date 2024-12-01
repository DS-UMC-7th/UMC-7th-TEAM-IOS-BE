package com.bookin.bookin.domain.review.repository;

import com.bookin.bookin.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Float calculateAverageRatingByBookId(@Param("bookId") Long bookId);
}

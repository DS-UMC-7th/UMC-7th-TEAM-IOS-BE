package com.bookin.bookin.domain.review.repository;


import com.bookin.bookin.domain.review.entity.ReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {
    List<ReviewTag> findByReviewId(Long reviewId);
}

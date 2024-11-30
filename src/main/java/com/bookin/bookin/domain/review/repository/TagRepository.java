package com.bookin.bookin.domain.review.repository;

import com.bookin.bookin.domain.review.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}

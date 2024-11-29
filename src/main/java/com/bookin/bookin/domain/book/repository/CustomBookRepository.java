package com.bookin.bookin.domain.book.repository;

import com.bookin.bookin.domain.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBookRepository {
    // 별점 높은 순 정렬
    Page<Book> findAllOrderByRatingDesc(Pageable pageable);

    // 리뷰 최신순 정렬
    Page<Book> findAllOrderByLatestReview(Pageable pageable);
}

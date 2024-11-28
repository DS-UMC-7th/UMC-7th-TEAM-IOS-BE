package com.bookin.bookin.domain.book.repository;

import com.bookin.bookin.domain.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // 별점 높은 순 정렬
    // 리뷰 최신순 정렬
}

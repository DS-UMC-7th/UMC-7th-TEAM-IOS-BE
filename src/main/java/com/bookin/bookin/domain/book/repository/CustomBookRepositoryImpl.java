package com.bookin.bookin.domain.book.repository;

import com.bookin.bookin.domain.book.entity.Book;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomBookRepositoryImpl implements CustomBookRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Book> findAllOrderByRatingDesc(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Book> findAllOrderByLatestReview(Pageable pageable) {
        return null;
    }
}

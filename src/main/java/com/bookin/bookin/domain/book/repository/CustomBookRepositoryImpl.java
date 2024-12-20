package com.bookin.bookin.domain.book.repository;

import com.bookin.bookin.domain.book.entity.Book;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bookin.bookin.domain.book.entity.QBook.*;
import static com.bookin.bookin.domain.review.entity.QReview.*;

@Repository
@RequiredArgsConstructor
public class CustomBookRepositoryImpl implements CustomBookRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Book> findAllOrderByRatingDesc(Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        List<Book> books = jpaQueryFactory
                .selectFrom(book)
                .where(builder)
                .orderBy(book.rating.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(book.count())
                .from(book)
                .where(builder).fetchOne();

        total = (total != null) ? total : 0L;

        return new PageImpl<>(books, pageable, total);
    }

    @Override
    public Page<Book> findAllOrderByLatestReview(Pageable pageable) {

        List<Book> books = jpaQueryFactory
                .selectFrom(book)
                .leftJoin(book.reviews, review)
                .groupBy(book.id)
                .orderBy(review.createdAt.max().desc().nullsLast(), book.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long total = jpaQueryFactory
                .select(book.countDistinct())
                .from(book)
                .fetchOne();

        total = (total != null) ? total : 0L;

        return new PageImpl<>(books, pageable, total);
    }

    @Override
    public Page<Book> findAllByOrderByReviewCountDesc(Pageable pageable) {

        List<Book> books = jpaQueryFactory
                .selectFrom(book)
                .leftJoin(book.reviews, review)
                .groupBy(book.id)
                .orderBy(review.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(book.count())
                .from(book)
                .fetchOne();

        total = (total != null) ? total : 0L;

        return new PageImpl<>(books, pageable, total);
    }
}

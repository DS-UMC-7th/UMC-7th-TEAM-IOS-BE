package com.bookin.bookin.domain.book.service;

import com.bookin.bookin.domain.book.dto.request.BookRequestDTO;
import com.bookin.bookin.domain.book.dto.response.BookDTO;
import com.bookin.bookin.domain.book.dto.response.BookDetailResponse;
import com.bookin.bookin.domain.book.dto.response.BookListResponse;
import com.bookin.bookin.domain.book.entity.Book;
import com.bookin.bookin.domain.book.repository.BookRepository;
import com.bookin.bookin.domain.book.repository.CustomBookRepository;
import com.bookin.bookin.global.apiPayload.code.exception.GeneralException;
import com.bookin.bookin.global.apiPayload.code.exception.handler.CustomHandler;
import com.bookin.bookin.global.apiPayload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final CustomBookRepository customBookRepository;
    private final BookRepository bookRepository;

    public BookListResponse getBooks(String sortedBy, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> books;

        if (sortedBy.equals("highest")) {
            books = customBookRepository.findAllOrderByRatingDesc(pageRequest);
        } else if (sortedBy.equals("latest")) {
            books = customBookRepository.findAllOrderByLatestReview(pageRequest);
        } else if (sortedBy.equals("popular")) {
            books = customBookRepository.findAllByOrderByReviewCountDesc(pageRequest);
        } else {
            books = bookRepository.findAll(pageRequest);
        }

        boolean isLast = books.isLast();
        int totalPage = books.getTotalPages();
        long totalElement = books.getTotalElements();

        List<BookDTO> bookDTOs = books.getContent().stream()
                .map(BookDTO::of)
                .toList();

        return BookListResponse.builder()
                .isLast(isLast)
                .totalPage(totalPage)
                .totalElement(totalElement)
                .books(bookDTOs)
                .build();
    }

    public Book saveBook(BookRequestDTO reqeust) {
        Book book = Book.builder()
                .title(reqeust.getTitle())
                .author(reqeust.getAuthor())
                .isbn(reqeust.getIsbn())
                .publisher(reqeust.getPublisher())
                .description(reqeust.getDescription())
                .publishedDate(reqeust.getPublishedDate())
                .image(reqeust.getImageUrl())
                .price(reqeust.getPrice())
                .rating(null)
                .build();

        return bookRepository.save(book);
    }

    public void updateBookRating(Book book, Float averageRating) {
        book.setRating(averageRating);
        bookRepository.save(book);
    }

    public BookDetailResponse getBookDetail(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new CustomHandler(ErrorStatus.BOOK_NOT_FOUND));
        return BookDetailResponse.of(book);
    }
}

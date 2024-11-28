package com.bookin.bookin.domain.book.service;

import com.bookin.bookin.domain.book.dto.response.BookDTO;
import com.bookin.bookin.domain.book.dto.response.BookListResponse;
import com.bookin.bookin.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    public BookListResponse getBooks(String sortedBy, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Object[]> filters;
        boolean isLast;
        int totalPage;
        long totalElement;
        List<BookDTO> books;

        if(sortedBy.equals("highest")) {

        } else if(sortedBy.equals("latest")){

        } else {

        }
        return null;
    }
}

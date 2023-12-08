package com.example.bookmanager.repository;

import com.example.bookmanager.domain.Book;
import com.example.bookmanager.domain.BookReviewInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookReviewInfoRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookReviewInfoRepository bookReviewInfoRepository;

    @Test
    void crudTest() {
        addBookReviewInfo();

        Book result = bookReviewInfoRepository
                .findById(1L)
                .orElseThrow(() -> new RuntimeException())
                .getBook();
        System.out.println("result => " + result);

        BookReviewInfo result2 = bookRepository.findById(1L)
                                            .orElseThrow(() -> new RuntimeException())
                                            .getBookReviewInfo();
        System.out.println("result2 => " + result2);
    }

    private Book addBook() {
        Book book = new Book().builder()
                .bookName("2023 정보처리 산업기사")
                .authorId(1L)
                .publisherId(1L)
                .category("자격증")
                .build();

        System.out.println("get book => " + bookRepository.findAll());
        return bookRepository.save(book);
    }

    private void addBookReviewInfo() {
        BookReviewInfo bookReviewInfo = new BookReviewInfo().builder()
                .book(addBook())
                .averageReviewScore(4.5f)
                .reviewCount(20)
                .build();
        bookReviewInfoRepository.save(bookReviewInfo);
        System.out.println("get book review => " + bookReviewInfoRepository.findAll());
    }
}
package com.example.bookreviews.service;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.mapper.BookMapper;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDto> findAllBooks(String title) {
        List<Book> books;

        if (title != null && !title.isEmpty()) {
            books = bookRepository.findByTitleContainingIgnoreCase(title);
        } else {
            books = bookRepository.findAll();
        }
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public BookDto findBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена с id: " + id));
        return bookMapper.toDto(book);
    }

    public void initData() {
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book("Clean Code", "Robert Martin", "Programming classic", "978-0132350884"));
            bookRepository.save(new Book("The Witcher", "Andrzej Sapkowski", "Fantasy novel", "978-0575084841"));
            bookRepository.save(new Book("Java for Dummies", "Barry Burd", "Good start", "978-1119235552"));
        }
    }
}
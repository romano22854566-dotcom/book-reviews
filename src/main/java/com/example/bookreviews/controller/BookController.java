package com.example.bookreviews.controller;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books") // Все запросы будут идти на http://localhost:8080/books
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
        // Запускаем создание тестовых данных
        bookService.initData();
    }

    // 1. GET endpoint с @RequestParam
    // Пример: /books?title=Witcher (вернет Ведьмака)
    // Пример: /books (вернет все книги)
    @GetMapping
    public List<BookDto> getAllBooks(@RequestParam(required = false) String title) {
        return bookService.findAllBooks(title);
    }

    // 2. GET endpoint с @PathVariable
    // Пример: /books/1 (вернет книгу с ID 1)
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }
}
package com.example.bookreviews.controller;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.service.BookService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public final class BookController {

    private final BookService bookService;

    public BookController(final BookService bookService) {
        this.bookService = bookService;
        bookService.initData();
    }

    @GetMapping
    public List<BookDto> getAllBooks(@RequestParam(required = false) final String title) {
        return bookService.findAllBooks(title);
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable final Long id) {
        return bookService.findBookById(id);
    }
}
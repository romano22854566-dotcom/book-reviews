package com.example.bookreviews.controller;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
        bookService.initData();
    }
    @GetMapping
    public List<BookDto> getAllBooks(@RequestParam(required = false) String title) {
        return bookService.findAllBooks(title);
    }
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }
}
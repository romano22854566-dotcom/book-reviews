package com.example.bookreviews.controller;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.service.BookService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping
    public BookDto createBook(@RequestParam final String title, @RequestParam final int pages) {
        return bookService.createBook(title, pages);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable final Long id,
                              @RequestParam final String title,
                              @RequestParam final int pages) {
        return bookService.updateBook(id, title, pages);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable final Long id) {
        bookService.deleteBook(id);
        return "Книга с id " + id + " удалена!";
    }

    @GetMapping("/demo/no-transaction")
    public String demoNoTrans() {
        try {
            bookService.demoWithoutTransaction();
        } catch (Exception e) {
            return "Ошибка! Но лог сохранен (частичное сохранение).";
        }
        return "Успех";
    }

    @GetMapping("/demo/with-transaction")
    public String demoTrans() {
        try {
            bookService.demoWithTransaction();
        } catch (Exception e) {
            return "Ошибка! Лог ОТКАТИЛСЯ (база чистая).";
        }
        return "Успех";
    }
}
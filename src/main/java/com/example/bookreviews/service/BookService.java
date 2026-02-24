package com.example.bookreviews.service;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.mapper.BookMapper;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    // Логика получения всех книг или поиска по названию
    public List<BookDto> findAllBooks(String title) {
        List<Book> books;

        // Если title передан и не пустой — ищем по названию
        if (title != null && !title.isEmpty()) {
            books = bookRepository.findByTitleContainingIgnoreCase(title);
        } else {
            // Иначе возвращаем всё
            books = bookRepository.findAll();
        }

        // Превращаем список Book в список BookDto
        return books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    // Логика получения одной книги по ID
    public BookDto findBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена с id: " + id));
        return bookMapper.toDto(book);
    }

    // Метод для заполнения базы тестовыми данными при старте
    public void initData() {
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book("Clean Code", "Robert Martin", "Programming classic", "978-0132350884"));
            bookRepository.save(new Book("The Witcher", "Andrzej Sapkowski", "Fantasy novel", "978-0575084841"));
            bookRepository.save(new Book("Java for Dummies", "Barry Burd", "Good start", "978-1119235552"));
        }
    }
}
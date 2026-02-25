package com.example.bookreviews.service;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.mapper.BookMapper;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public final class BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookService(final BookRepository bookRepository, final BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDto> findAllBooks(final String title) {
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

    public BookDto findBookById(final Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена с id: " + id));
        return bookMapper.toDto(book);
    }

    public void initData() {
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book("Преступление и наказание", "Федор Достоевский",
                    "Глубокий психологический роман о цене идеи.", "978-5-389-04924-6"));

            bookRepository.save(new Book("Мастер и Маргарита", "Михаил Булгаков",
                    "Мистическая история, объединяющая древний мир и Москву 30-х.",
                    "978-5-17-090408-2"));

            bookRepository.save(new Book("Война и мир", "Лев Толстой",
                    "Великая эпопея о судьбах людей на фоне войны с Наполеоном.",
                    "978-5-699-12014-7"));
        }
    }
}
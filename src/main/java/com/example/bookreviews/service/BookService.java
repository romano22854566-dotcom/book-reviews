package com.example.bookreviews.service;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.mapper.BookMapper;
import com.example.bookreviews.model.Author;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.model.Comment;
import com.example.bookreviews.model.Log;
import com.example.bookreviews.model.Role;
import com.example.bookreviews.model.Status;
import com.example.bookreviews.model.User;
import com.example.bookreviews.repository.BookRepository;
import com.example.bookreviews.repository.LogRepository;
import com.example.bookreviews.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final LogRepository logRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    public BookService(final BookRepository bookRepository, final LogRepository logRepository,
                       final UserRepository userRepository, final BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDto> findAllBooks(final String title) {
        List<Book> books = (title != null && !title.isEmpty())
                ? bookRepository.findByTitleContainingIgnoreCase(title)
                : bookRepository.findAll();
        return books.stream().map(bookMapper::toDto).toList();
    }

    public BookDto findBookById(final Long id) {
        return bookRepository.findWithDetailsById(id).map(bookMapper::toDto)
                .orElseThrow(() -> {
                    logRepository.save(new Log(Status.FAILURE, "Неудачная попытка найти . ID не существует: " + id));
                    return new RuntimeException("Книга не найдена с id: " + id);
                });
    }

    public BookDto createBook(final String title, final int pages) {
        Book book = new Book(title, pages);
        Book savedBook = bookRepository.save(book);

        logRepository.save(new Log(Status.SUCCESS, "Админ успешно создал новую книгу: " + title));

        return bookMapper.toDto(savedBook);
    }

    public BookDto updateBook(final Long id, final String title, final int pages) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена с id: " + id));

        book.setTitle(title);
        book.setPages(pages);
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    public void deleteBook(final Long id) {
        bookRepository.deleteById(id);
    }

    public void demoWithoutTransaction() {
        logRepository.save(new Log(Status.IN_PROGRESS, "Попытка сохранить БЕЗ транзакции"));
        throw new RuntimeException("Искусственная ошибка БД!");
    }

    @Transactional
    public void demoWithTransaction() {
        logRepository.save(new Log(Status.IN_PROGRESS, "Попытка сохранить С транзакцией"));
        throw new RuntimeException("Искусственная ошибка БД!");
    }
    
    public void initData() {
        if (bookRepository.count() == 0) {
            Author dostoevsky = new Author("Федор Достоевский");
            User admin = new User("Иван_Админ", Role.ADMIN);
            userRepository.save(admin);

            final int pagesCount = 500;
            Book book = new Book("Преступление и наказание", pagesCount);
            book.getAuthors().add(dostoevsky);
            dostoevsky.getBooks().add(book);

            Comment comment = new Comment("Очень глубокая книга!", book, admin);
            book.getComments().add(comment);

            bookRepository.save(book);
        }
    }
}
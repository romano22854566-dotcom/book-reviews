package com.example.bookreviews.service;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.dto.BookRequest;
import com.example.bookreviews.mapper.BookMapper;
import com.example.bookreviews.model.Author;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.model.Category;
import com.example.bookreviews.model.Log;
import com.example.bookreviews.model.Status;
import com.example.bookreviews.model.User;
import com.example.bookreviews.repository.AuthorRepository;
import com.example.bookreviews.repository.BookRepository;
import com.example.bookreviews.repository.CategoryRepository;
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
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private static final String BOOK_NOT_FOUND_MSG = "Книга не найдена с id: ";

    public BookService(final BookRepository bookRepository, final LogRepository logRepository,
                       final UserRepository userRepository, final BookMapper bookMapper,
                       final AuthorRepository authorRepository, final CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.bookMapper = bookMapper;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
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
                    User admin = userRepository.findById(1L).orElse(null);
                    logRepository.save(new Log(Status.FAILURE, "Неудачная попытка найти книгу: " + id, admin));
                    return new IllegalStateException(BOOK_NOT_FOUND_MSG + id);
                });
    }

    @Transactional
    public BookDto createBook(final BookRequest request) {
        Book book = new Book(request.title(), request.pages(), request.publicationYear());

        if (request.authorIds() != null && !request.authorIds().isEmpty()) {
            List<Author> authors = authorRepository.findAllById(request.authorIds());
            book.getAuthors().addAll(authors);
        }

        if (request.categoryIds() != null && !request.categoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.categoryIds());
            book.getCategories().addAll(categories);
        }

        Book savedBook = bookRepository.save(book);

        User admin = userRepository.findById(1L).orElse(null);
        logRepository.save(new Log(Status.SUCCESS, "Успешно создана книга: " + request.title(), admin));

        return bookMapper.toDto(savedBook);
    }

    @Transactional
    public BookDto updateBook(final Long id, final BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND_MSG + id));

        book.setTitle(request.title());
        book.setPages(request.pages());
        book.setPublicationYear(request.publicationYear());

        book.getAuthors().clear();
        if (request.authorIds() != null && !request.authorIds().isEmpty()) {
            List<Author> authors = authorRepository.findAllById(request.authorIds());
            book.getAuthors().addAll(authors);
        }

        book.getCategories().clear();
        if (request.categoryIds() != null && !request.categoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(request.categoryIds());
            book.getCategories().addAll(categories);
        }

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    @Transactional
    public BookDto patchBook(final Long id, final BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND_MSG + id));

        if (request.title() != null) {
            book.setTitle(request.title());
        }

        if (request.pages() > 0) {
            book.setPages(request.pages());
        }

        if (request.publicationYear() != null) {
            book.setPublicationYear(request.publicationYear());
        }

        if (request.authorIds() != null) {
            book.getAuthors().clear();
            if (!request.authorIds().isEmpty()) {
                List<Author> authors = authorRepository.findAllById(request.authorIds());
                book.getAuthors().addAll(authors);
            }
        }

        if (request.categoryIds() != null) {
            book.getCategories().clear();
            if (!request.categoryIds().isEmpty()) {
                List<Category> categories = categoryRepository.findAllById(request.categoryIds());
                book.getCategories().addAll(categories);
            }
        }

        Book patchedBook = bookRepository.save(book);
        return bookMapper.toDto(patchedBook);
    }

    public void deleteBook(final Long id) {
        bookRepository.deleteById(id);
    }

    public void demoWithoutTransaction() {
        User admin = userRepository.findById(1L).orElse(null);
        logRepository.save(new Log(Status.IN_PROGRESS, "Попытка сохранить БЕЗ транзакции", admin));
        throw new IllegalStateException("Искусственная ошибка БД!");
    }

    @Transactional
    public void demoWithTransaction() {
        User admin = userRepository.findById(1L).orElse(null);
        logRepository.save(new Log(Status.IN_PROGRESS, "Попытка сохранить С транзакцией", admin));
        throw new IllegalStateException("Искусственная ошибка БД!");
    }
}
package com.example.bookreviews.service;

import com.example.bookreviews.cache.BookCacheManager;
import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.dto.BookRequest;
import com.example.bookreviews.exception.ResourceNotFoundException;
import com.example.bookreviews.mapper.BookMapper;
import com.example.bookreviews.model.Author;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.model.Log;
import com.example.bookreviews.model.Role;
import com.example.bookreviews.model.User;
import com.example.bookreviews.repository.AuthorRepository;
import com.example.bookreviews.repository.BookRepository;
import com.example.bookreviews.repository.CategoryRepository;
import com.example.bookreviews.repository.LogRepository;
import com.example.bookreviews.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private LogRepository logRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookCacheManager bookCacheManager;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private BookDto testBookDto;
    private User adminUser;

    @BeforeEach
    void setUp() {
        testBook = new Book("Война и мир", 1225, 1869);
        testBook.setId(1L);

        testBookDto = new BookDto(
                1L, "Война и мир", 1225, 1869,
                List.of("Лев Толстой"),
                List.of("Роман"),
                Collections.emptyList());

        adminUser = new User("Admin", Role.ADMIN);
        adminUser.setId(1L);
    }

    @Test
    @DisplayName("findAllBooks — без фильтра")
    void findAllBooks_noFilter() {
        when(bookRepository.findAllByOrderByIdAsc())
                .thenReturn(List.of(testBook));
        when(bookMapper.toDto(testBook))
                .thenReturn(testBookDto);

        List<BookDto> result =
                bookService.findAllBooks(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Война и мир", result.get(0).title());
    }

    @Test
    @DisplayName("findAllBooks — с фильтром по названию")
    void findAllBooks_withTitle() {
        when(bookRepository
                .findByTitleContainingIgnoreCase("Война"))
                .thenReturn(List.of(testBook));
        when(bookMapper.toDto(testBook))
                .thenReturn(testBookDto);

        List<BookDto> result =
                bookService.findAllBooks("Война");

        assertEquals(1, result.size());
        verify(bookRepository)
                .findByTitleContainingIgnoreCase("Война");
    }

    @Test
    @DisplayName("findAllBooks — пустой результат")
    void findAllBooks_empty() {
        when(bookRepository.findAllByOrderByIdAsc())
                .thenReturn(Collections.emptyList());

        List<BookDto> result =
                bookService.findAllBooks(null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findBookById — успех")
    void findBookById_success() {
        when(bookRepository.findWithDetailsById(1L))
                .thenReturn(Optional.of(testBook));
        when(bookMapper.toDto(testBook))
                .thenReturn(testBookDto);

        BookDto result = bookService.findBookById(1L);

        assertNotNull(result);
        assertEquals("Война и мир", result.title());
    }

    @Test
    @DisplayName("findBookById — не найдена, лог записывается")
    void findBookById_notFound() {
        when(bookRepository.findWithDetailsById(99L))
                .thenReturn(Optional.empty());
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(adminUser));
        when(logRepository.save(any(Log.class)))
                .thenReturn(new Log());

        assertThrows(ResourceNotFoundException.class,
                () -> bookService.findBookById(99L));
        verify(logRepository).save(any(Log.class));
    }

    @Test
    @DisplayName("createBook — успех без авторов и категорий")
    void createBook_success() {
        BookRequest request = new BookRequest(
                "Новая книга", 300, 2023, null, null);

        when(bookRepository.save(any(Book.class)))
                .thenReturn(testBook);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(adminUser));
        when(logRepository.save(any(Log.class)))
                .thenReturn(new Log());
        when(bookMapper.toDto(any(Book.class)))
                .thenReturn(testBookDto);

        BookDto result = bookService.createBook(request);

        assertNotNull(result);
        verify(bookRepository).save(any(Book.class));
        verify(bookCacheManager).invalidate();
    }

    @Test
    @DisplayName("createBook — с авторами")
    void createBook_withAuthors() {
        Author author = new Author("Лев", "Толстой");
        author.setId(1L);

        BookRequest request = new BookRequest(
                "Книга", 300, 2023,
                List.of(1L), null);

        when(authorRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(author));
        when(bookRepository.save(any(Book.class)))
                .thenReturn(testBook);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(adminUser));
        when(logRepository.save(any(Log.class)))
                .thenReturn(new Log());
        when(bookMapper.toDto(any(Book.class)))
                .thenReturn(testBookDto);

        BookDto result = bookService.createBook(request);

        assertNotNull(result);
        verify(authorRepository).findAllById(List.of(1L));
    }

    @Test
    @DisplayName("deleteBook — успех")
    void deleteBook_success() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
        verify(bookCacheManager).invalidate();
    }

    @Test
    @DisplayName("demoWithoutTransaction — бросает исключение, "
            + "лог сохраняется")
    void demoWithoutTransaction_throwsAfterSave() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(adminUser));
        when(logRepository.save(any(Log.class)))
                .thenReturn(new Log());

        assertThrows(IllegalStateException.class,
                () -> bookService.demoWithoutTransaction());
        verify(logRepository).save(any(Log.class));
    }

    @Test
    @DisplayName("demoWithTransaction — бросает исключение")
    void demoWithTransaction_throws() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(adminUser));
        when(logRepository.save(any(Log.class)))
                .thenReturn(new Log());

        assertThrows(IllegalStateException.class,
                () -> bookService.demoWithTransaction());
    }
}

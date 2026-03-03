package com.example.bookreviews.service;

import com.example.bookreviews.model.Author;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.model.Comment;
import com.example.bookreviews.model.Log;
import com.example.bookreviews.model.Role;
import com.example.bookreviews.model.Status;
import com.example.bookreviews.model.User;
import com.example.bookreviews.repository.AuthorRepository;
import com.example.bookreviews.repository.BookRepository;
import com.example.bookreviews.repository.LogRepository;
import com.example.bookreviews.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final LogRepository logRepository;

    public DataInitializer(UserRepository userRepository, AuthorRepository authorRepository,
                           BookRepository bookRepository, LogRepository logRepository) {
        this.userRepository = userRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.logRepository = logRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (bookRepository.count() == 0) {

            User admin = new User("Роман", Role.ADMIN);
            User user1 = new User("Анна", Role.USER);
            User user2 = new User("Петр", Role.USER);
            User user3 = new User("Мария", Role.USER);
            User user4 = new User("Алексей", Role.USER);
            userRepository.saveAll(List.of(admin, user1, user2, user3, user4));

            Author author1 = new Author("Лев Толстой");
            Author author2 = new Author("Федор Достоевский");
            Author author3 = new Author("Михаил Булгаков");
            Author author4 = new Author("Александр Пушкин");
            Author author5 = new Author("Николай Гоголь");
            authorRepository.saveAll(List.of(author1, author2, author3, author4, author5));

            final int pages1 = 1200;
            final int pages2 = 500;
            final int pages3 = 450;
            final int pages4 = 320;
            final int pages5 = 350;

            Book book1 = new Book("Война и мир", pages1);
            Book book2 = new Book("Преступление и наказание", pages2);
            Book book3 = new Book("Мастер и Маргарита", pages3);
            Book book4 = new Book("Капитанская дочка", pages4);
            Book book5 = new Book("Мертвые души", pages5);

            book1.getAuthors().add(author1);
            book2.getAuthors().add(author2);
            book3.getAuthors().add(author3);
            book4.getAuthors().add(author4);
            book5.getAuthors().add(author5);
            book5.getAuthors().add(author4);

            book1.getComments().add(new Comment("Слишком длинно, но интересно.", book1, user1));
            book2.getComments().add(new Comment("Заставляет задуматься .", book2, user2));
            book3.getComments().add(new Comment("Отлично", book3, user3));
            book4.getComments().add(new Comment("Классика на все времена", book4, user4));
            book5.getComments().add(new Comment("Хорошее произведение", book5, admin));

            bookRepository.saveAll(List.of(book1, book2, book3, book4, book5));

            logRepository.saveAll(List.of(
                    new Log(Status.SUCCESS, "Система инициализирована", admin),
                    new Log(Status.SUCCESS, "Загружены пользователи", admin),
                    new Log(Status.SUCCESS, "Загружены авторы", admin),
                    new Log(Status.SUCCESS, "Загружены книги", admin),
                    new Log(Status.SUCCESS, "Тестовые данные успешно созданы", admin)
            ));

            System.out.println("✅ База данных успешно заполнена 5-ю тестовыми записями для каждой таблицы!");
        }
    }
}
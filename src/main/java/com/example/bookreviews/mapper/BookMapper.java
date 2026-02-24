package com.example.bookreviews.mapper;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    // Превращает сущность из БД в DTO для ответа
    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription()
        );
    }

    // В будущем сюда добавим метод toEntity, когда будем создавать книги
}
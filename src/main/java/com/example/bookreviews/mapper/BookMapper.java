package com.example.bookreviews.mapper;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.model.Author;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class BookMapper {

    public BookDto toDto(final Book book) {
        // Достаем имена авторов из объектов Author
        List<String> authorNames = book.getAuthors().stream()
                .map(Author::getName)
                .toList();

        // Достаем тексты комментариев из объектов Comment
        List<String> commentTexts = book.getComments().stream()
                .map(Comment::getText)
                .toList();

        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getPages(),
                authorNames,
                commentTexts
        );
    }
}
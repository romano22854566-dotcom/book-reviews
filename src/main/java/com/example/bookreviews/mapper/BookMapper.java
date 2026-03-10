package com.example.bookreviews.mapper;

import com.example.bookreviews.dto.BookDto;
import com.example.bookreviews.model.Author;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.model.Category;
import com.example.bookreviews.model.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class BookMapper {

    public BookDto toDto(final Book book) {
        List<String> authorNames = book.getAuthors().stream()
                .map(Author::getName)
                .toList();

        List<String> categoryNames = book.getCategories().stream()
                .map(Category::getName)
                .toList();

        List<String> commentTexts = book.getComments().stream()
                .map(Comment::getText)
                .toList();

        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getPages(),
                book.getPublicationYear(),
                authorNames,
                categoryNames,
                commentTexts
        );
    }
}
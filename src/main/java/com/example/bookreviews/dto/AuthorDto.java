package com.example.bookreviews.dto;

import java.util.List;

public final class AuthorDto {
    private final Long id;
    private final String name;
    private final List<String> books; // Добавили список книг

    public AuthorDto(final Long id, final String name, final List<String> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getBooks() {
        return books;
    }
}
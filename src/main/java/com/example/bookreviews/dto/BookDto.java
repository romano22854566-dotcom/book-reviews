package com.example.bookreviews.dto;

public final class BookDto {

    private Long id;

    private String title;

    private String author;

    private String description;

    public BookDto(final Long id, final String title,
                   final String author, final String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }
}
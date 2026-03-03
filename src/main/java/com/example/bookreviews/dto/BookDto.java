package com.example.bookreviews.dto;

import java.util.List;

public final class BookDto {
    private final Long id;
    private final String title;
    private final int pages;
    private final List<String> authors;
    private final List<String> comments;

    public BookDto(Long id, String title, int pages, List<String> authors, List<String> comments) {
        this.id = id;
        this.title = title;
        this.pages = pages;
        this.authors = authors;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getPages() {
        return pages;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getComments() {
        return comments;
    }
}
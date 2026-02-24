package com.example.bookreviews.dto;

public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String description;

    public BookDto(Long id, String title, String author, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
}
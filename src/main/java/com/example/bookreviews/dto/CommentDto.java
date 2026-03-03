package com.example.bookreviews.dto;

public final class CommentDto {
    private final Long id;
    private final String text;
    private final String bookTitle;
    private final String authorName; // Имя пользователя, который оставил коммент

    public CommentDto(final Long id, final String text, final String bookTitle, final String authorName) {
        this.id = id;
        this.text = text;
        this.bookTitle = bookTitle;
        this.authorName = authorName;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

}
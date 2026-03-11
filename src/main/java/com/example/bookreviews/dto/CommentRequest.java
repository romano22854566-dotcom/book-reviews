package com.example.bookreviews.dto;

public record CommentRequest(String text, Integer rating, Long bookId, Long userId) {
}
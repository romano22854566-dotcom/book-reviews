package com.example.bookreviews.dto;

public record CommentRequest(String text, Long bookId, Long userId) {

}
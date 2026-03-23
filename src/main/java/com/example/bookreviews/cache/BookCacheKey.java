package com.example.bookreviews.cache;

// Record автоматически создает правильные equals() и hashCode(),
// что полностью удовлетворяет условию лабораторной работы!
public record BookCacheKey(
        String authorLastName,
        String categoryName,
        Integer rating,
        int page,
        int size
) {
}
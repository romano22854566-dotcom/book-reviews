package com.example.bookreviews.dto;

import java.util.List;

public record BookDto(Long id, String title, int pages, List<String> authors, List<String> comments) {
}
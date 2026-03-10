package com.example.bookreviews.dto;

import java.util.List;

public record BookRequest(
        String title,
        int pages,
        Integer publicationYear,
        List<Long> authorIds,
        List<Long> categoryIds
) {

}
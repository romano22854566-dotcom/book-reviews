package com.example.bookreviews.dto;

import java.util.List;

public final class UserDto {
    private final Long id;
    private final String name;
    private final String role;
    private final List<String> comments; // Добавили список комментариев

    public UserDto(final Long id, final String name, final String role, final List<String> comments) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public List<String> getComments() {
        return comments;
    }
}
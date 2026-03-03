package com.example.bookreviews.dto;

public final class LogDto {
    private final Long id;
    private final String status;
    private final String date; // Отдаем дату как красивую строчку
    private final String body;

    public LogDto(final Long id, final String status, final String date, final String body) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

}
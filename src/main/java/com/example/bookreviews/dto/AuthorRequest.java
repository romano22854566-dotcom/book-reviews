package com.example.bookreviews.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на создание/обновление автора")
public record AuthorRequest(

        @NotBlank(message = "Имя автора не может быть пустым")
        @Size(max = 50, message = "Имя не может превышать 50 символов")
        @Schema(description = "Имя автора", example = "Лев")
        String firstName,

        @NotBlank(message = "Фамилия автора не может быть пустой")
        @Size(max = 50, message = "Фамилия не может превышать 50 символов")
        @Schema(description = "Фамилия автора", example = "Толстой")
        String lastName
) {
}
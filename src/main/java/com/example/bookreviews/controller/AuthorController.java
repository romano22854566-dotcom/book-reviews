package com.example.bookreviews.controller;

import com.example.bookreviews.dto.AuthorDto;
import com.example.bookreviews.service.AuthorService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authors")
public final class AuthorController {
    private final AuthorService authorService;

    public AuthorController(final AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @PostMapping
    public AuthorDto createAuthor(@RequestParam final String name) {
        return authorService.createAuthor(name);
    }

    @DeleteMapping("/{id}")
    public String deleteAuthor(@PathVariable final Long id) {
        authorService.deleteAuthor(id);
        return "Автор удален!";
    }
}
package com.example.bookreviews.controller;

import com.example.bookreviews.dto.AuthorDto;
import com.example.bookreviews.dto.AuthorRequest;
import com.example.bookreviews.service.AuthorService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authors") // В Postman будет доступен по /api/authors
public final class AuthorController {

    private final AuthorService authorService;

    public AuthorController(final AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable final Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public AuthorDto createAuthor(@RequestBody final AuthorRequest request) {
        return authorService.createAuthor(request);
    }

    @PutMapping("/{id}")
    public AuthorDto updateAuthor(@PathVariable final Long id, @RequestBody final AuthorRequest request) {
        return authorService.updateAuthor(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteAuthor(@PathVariable final Long id) {
        authorService.deleteAuthor(id);
        return "Автор удален!";
    }
}
package com.example.bookreviews.service;

import com.example.bookreviews.dto.AuthorDto;
import com.example.bookreviews.mapper.AuthorMapper;
import com.example.bookreviews.model.Author;
import com.example.bookreviews.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(final AuthorRepository authorRepository, final AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream().map(authorMapper::toDto).toList();
    }

    public AuthorDto createAuthor(final String name) {
        Author author = new Author(name);
        return authorMapper.toDto(authorRepository.save(author));
    }

    public void deleteAuthor(final Long id) {
        authorRepository.deleteById(id);
    }
}
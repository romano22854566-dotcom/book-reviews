package com.example.bookreviews.repository;

import com.example.bookreviews.model.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"books"})
    List<Author> findAll();
}
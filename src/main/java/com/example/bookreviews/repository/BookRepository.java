package com.example.bookreviews.repository;

import com.example.bookreviews.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"authors", "comments", "categories"})
    List<Book> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = {"authors", "comments", "categories"})
    List<Book> findByTitleContainingIgnoreCase(String title);

    @NonNull
    @EntityGraph(attributePaths = {"authors", "comments", "categories"})
    Optional<Book> findWithDetailsById(Long id);
}
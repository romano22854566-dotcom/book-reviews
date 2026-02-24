package com.example.bookreviews.repository;

import com.example.bookreviews.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Спринг сам поймет, что нужно искать по полю Title (игнорируя регистр букв)
    List<Book> findByTitleContainingIgnoreCase(String title);
}
package com.example.bookreviews.repository;

import com.example.bookreviews.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"comments"})
    List<User> findAll();
}
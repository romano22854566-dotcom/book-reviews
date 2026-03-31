package com.example.bookreviews.service;

import com.example.bookreviews.cache.BookCacheManager;
import com.example.bookreviews.dto.CommentDto;
import com.example.bookreviews.dto.CommentRequest;
import com.example.bookreviews.exception.ResourceNotFoundException;
import com.example.bookreviews.mapper.CommentMapper;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.model.Comment;
import com.example.bookreviews.model.User;
import com.example.bookreviews.repository.BookRepository;
import com.example.bookreviews.repository.CommentRepository;
import com.example.bookreviews.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final BookCacheManager bookCacheManager;

    public CommentService(
            final CommentRepository commentRepository,
            final BookRepository bookRepository,
            final UserRepository userRepository,
            final CommentMapper commentMapper,
            final BookCacheManager bookCacheManager) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.bookCacheManager = bookCacheManager;
    }

    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toDto).toList();
    }

    public CommentDto getCommentById(final Long id) {
        Comment comment =
                commentRepository.findWithDetailsById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Комментарий не найден с id: "
                                                + id));
        return commentMapper.toDto(comment);
    }

    @Transactional
    public CommentDto createComment(
            final CommentRequest request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Книга не найдена"));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Пользователь не найден"));

        Integer rating = request.rating();
        if (rating == null || rating < 1 || rating > 10) {
            throw new IllegalArgumentException(
                    "Оценка должна быть от 1 до 10");
        }

        Comment comment = new Comment(
                request.text(), rating, book, user);
        Comment saved = commentRepository.save(comment);
        bookCacheManager.invalidate();
        return commentMapper.toDto(saved);
    }

    @Transactional
    public CommentDto updateComment(final Long id,
                                    final CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Комментарий не найден с id: " + id));

        if (request.text() != null) {
            comment.setText(request.text());
        }
        if (request.rating() != null) {
            if (request.rating() < 1
                    || request.rating() > 10) {
                throw new IllegalArgumentException(
                        "Оценка должна быть от 1 до 10");
            }
            comment.setRating(request.rating());
        }

        Comment updated = commentRepository.save(comment);
        bookCacheManager.invalidate();
        return commentMapper.toDto(updated);
    }

    public void deleteComment(final Long id) {
        commentRepository.deleteById(id);
        bookCacheManager.invalidate();
    }
}
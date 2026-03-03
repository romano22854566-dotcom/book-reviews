package com.example.bookreviews.service;

import com.example.bookreviews.dto.CommentDto;
import com.example.bookreviews.mapper.CommentMapper;
import com.example.bookreviews.model.Book;
import com.example.bookreviews.model.Comment;
import com.example.bookreviews.model.User;
import com.example.bookreviews.repository.BookRepository;
import com.example.bookreviews.repository.CommentRepository;
import com.example.bookreviews.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentService(final CommentRepository commentRepository, final BookRepository bookRepository,
                          final UserRepository userRepository, final CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream().map(commentMapper::toDto).toList();
    }

    public CommentDto createComment(final Long bookId, final Long userId, final String text) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Comment comment = new Comment(text, book, user);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    public CommentDto updateComment(final Long id, final String newText) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));
        comment.setText(newText);
        return commentMapper.toDto(commentRepository.save(comment));
    }

    public void deleteComment(final Long id) {
        commentRepository.deleteById(id);
    }
}
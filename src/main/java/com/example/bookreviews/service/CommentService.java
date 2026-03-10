package com.example.bookreviews.service;

import com.example.bookreviews.dto.CommentDto;
import com.example.bookreviews.dto.CommentRequest;
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

    public CommentDto getCommentById(final Long id) {
        Comment comment = commentRepository.findWithDetailsById(id)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден с id: " + id));
        return commentMapper.toDto(comment);
    }

    @Transactional
    public CommentDto createComment(final CommentRequest request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Comment comment = new Comment(request.text(), book, user);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toDto(savedComment);
    }

    @Transactional
    public CommentDto updateComment(final Long id, final CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден с id: " + id));

        comment.setText(request.text());

        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }

    public void deleteComment(final Long id) {
        commentRepository.deleteById(id);
    }
}
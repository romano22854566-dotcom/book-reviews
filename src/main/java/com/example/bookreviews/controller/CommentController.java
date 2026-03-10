package com.example.bookreviews.controller;

import com.example.bookreviews.dto.CommentDto;
import com.example.bookreviews.dto.CommentRequest;
import com.example.bookreviews.service.CommentService;
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
@RequestMapping("/comments")
public final class CommentController {

    private final CommentService commentService;

    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDto> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public CommentDto getCommentById(@PathVariable final Long id) {
        return commentService.getCommentById(id);
    }

    @PostMapping
    public CommentDto createComment(@RequestBody final CommentRequest request) {
        return commentService.createComment(request);
    }

    @PutMapping("/{id}")
    public CommentDto updateComment(@PathVariable final Long id, @RequestBody final CommentRequest request) {
        return commentService.updateComment(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable final Long id) {
        commentService.deleteComment(id);
        return "Комментарий удален!";
    }
}
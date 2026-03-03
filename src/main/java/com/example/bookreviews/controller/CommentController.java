package com.example.bookreviews.controller;

import com.example.bookreviews.dto.CommentDto;
import com.example.bookreviews.service.CommentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping
    public CommentDto createComment(@RequestParam final Long bookId,
                                    @RequestParam final Long userId,
                                    @RequestParam final String text) {
        return commentService.createComment(bookId, userId, text);
    }

    @PutMapping("/{id}")
    public CommentDto updateComment(@PathVariable final Long id, @RequestParam final String text) {
        return commentService.updateComment(id, text);
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable final Long id) {
        commentService.deleteComment(id);
        return "Комментарий удален!";
    }
}
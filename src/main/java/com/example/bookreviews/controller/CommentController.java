package com.example.bookreviews.controller;

import com.example.bookreviews.dto.CommentDto;
import com.example.bookreviews.dto.CommentRequest;
import com.example.bookreviews.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Комментарии",
        description = "CRUD-операции с комментариями")
public final class CommentController {

    private final CommentService commentService;

    public CommentController(
            final CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    @Operation(summary = "Получить все комментарии")
    public List<CommentDto> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить комментарий по ID")
    public CommentDto getCommentById(
            @PathVariable final Long id) {
        return commentService.getCommentById(id);
    }

    @PostMapping
    @Operation(summary = "Создать комментарий")
    public CommentDto createComment(
            @Valid @RequestBody final CommentRequest request) {
        return commentService.createComment(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить комментарий")
    public CommentDto updateComment(
            @PathVariable final Long id,
            @Valid @RequestBody final CommentRequest request) {
        return commentService.updateComment(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить комментарий")
    public String deleteComment(@PathVariable final Long id) {
        commentService.deleteComment(id);
        return "Комментарий удален!";
    }
}
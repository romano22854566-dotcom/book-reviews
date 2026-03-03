package com.example.bookreviews.controller;

import com.example.bookreviews.dto.LogDto;
import com.example.bookreviews.service.LogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logs")
public final class LogController {
    private final LogService logService;

    public LogController(final LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public List<LogDto> getAllLogs() {
        return logService.getAllLogs();
    }
}
package com.example.bookreviews.controller;

import com.example.bookreviews.dto.UserDto;
import com.example.bookreviews.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public final class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDto createUser(@RequestParam final String name) {
        return userService.createUser(name);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable final Long id) {
        userService.deleteUser(id);
        return "Пользователь удален!";
    }
}
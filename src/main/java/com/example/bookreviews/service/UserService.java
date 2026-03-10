package com.example.bookreviews.service;

import com.example.bookreviews.dto.UserDto;
import com.example.bookreviews.dto.UserRequest;
import com.example.bookreviews.mapper.UserMapper;
import com.example.bookreviews.model.Role;
import com.example.bookreviews.model.User;
import com.example.bookreviews.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    public UserDto getUserById(final Long id) {
        User user = userRepository.findWithDetailsById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + id));
        return userMapper.toDto(user);
    }

    public UserDto createUser(final UserRequest request) {
        Role role = (request.role() != null) ? request.role() : Role.USER;

        User user = new User(request.name(), role);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto updateUser(final Long id, final UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + id));

        user.setName(request.name());
        if (request.role() != null) {
            user.setRole(request.role());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }
}
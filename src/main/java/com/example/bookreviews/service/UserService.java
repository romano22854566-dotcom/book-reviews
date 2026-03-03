package com.example.bookreviews.service;

import com.example.bookreviews.dto.UserDto;
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

    public UserDto createUser(final String name) {
        User user = new User(name, Role.USER);
        return userMapper.toDto(userRepository.save(user));
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }
}
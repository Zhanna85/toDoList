package com.example.toDoList.service;

import com.example.toDoList.dto.NewUserDto;
import com.example.toDoList.dto.UpdateUserDto;
import com.example.toDoList.dto.UserDto;

public interface UserService {
    UserDto createUser(NewUserDto userDto);

    UserDto updateUser(Long userId, UpdateUserDto userDto);

    void deleteUserById(Long userId);

    UserDto getUserById(Long userId);
}
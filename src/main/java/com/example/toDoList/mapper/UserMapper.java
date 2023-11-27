package com.example.toDoList.mapper;

import com.example.toDoList.dto.NewUserDto;
import com.example.toDoList.dto.UserDto;
import com.example.toDoList.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toUser(NewUserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
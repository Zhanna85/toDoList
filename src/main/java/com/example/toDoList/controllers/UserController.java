package com.example.toDoList.controllers;

import com.example.toDoList.dto.NewUserDto;
import com.example.toDoList.dto.UpdateUserDto;
import com.example.toDoList.dto.UserDto;
import com.example.toDoList.service.UserService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto saveUser(@Valid @RequestBody NewUserDto userDto) {
        log.info("Creating user {}", userDto);
        return userService.createUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable(value = "userId") Long userId) {
        log.info("Get user by id {}", userId);
        return userService.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable(value = "userId") Long userId,
                              @Valid @RequestBody UpdateUserDto userDto) {
        log.info("Updating user id {}, {}", userId, userDto);
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "userId") Long userId) {
        log.info("Deleting user by Id {}", userId);
        userService.deleteUserById(userId);
    }
}
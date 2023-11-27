package com.example.toDoList.service;

import com.example.toDoList.dto.NewUserDto;
import com.example.toDoList.dto.UpdateUserDto;
import com.example.toDoList.dto.UserDto;
import com.example.toDoList.exception.NotFoundException;
import com.example.toDoList.mapper.UserMapper;
import com.example.toDoList.model.User;
import com.example.toDoList.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.toDoList.util.Message.*;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDto createUser(NewUserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        log.info(ADD_MODEL.getMessage(), user);
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UpdateUserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(MODEL_NOT_FOUND.getMessage() + userId));
        final String email = userDto.getEmail();
        final String name = userDto.getName();

        if (email != null && !user.getEmail().equals(email)) {
            user.setEmail(email);
        }
        if (name != null) {
            user.setName(name);
        }

        User updatedUser = userRepository.save(user);
        log.info(UPDATED_MODEL.getMessage(), userId, user);
        return UserMapper.toUserDto(updatedUser);
    }

    @Transactional
    @Override
    public void deleteUserById(Long userId) {
        log.info(DELETE_MODEL.getMessage(), userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + userId));
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + userId));
        log.info(GET_MODEL_BY_ID.getMessage(), user);
        return UserMapper.toUserDto(user);
    }
}
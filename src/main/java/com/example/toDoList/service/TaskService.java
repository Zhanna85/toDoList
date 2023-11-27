package com.example.toDoList.service;

import com.example.toDoList.dto.NewTaskDto;
import com.example.toDoList.dto.TaskDto;
import com.example.toDoList.dto.UpdateTaskDto;
import com.example.toDoList.util.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    TaskDto createdTask(Long userId, NewTaskDto task);

    TaskDto getTaskById(Long userId, Long taskId);

    List<TaskDto> getTasksWithParameters(Long userId, List<TaskStatus> status, String text, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Integer from, Integer size);

    TaskDto updateTaskById(UpdateTaskDto taskDto, Long userId, Long taskId);

    void deleteTaskById(Long userId, Long taskId);

    void deleteAllTaskByUserId(Long userId);
}
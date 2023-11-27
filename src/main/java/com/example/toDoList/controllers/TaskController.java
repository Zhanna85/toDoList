package com.example.toDoList.controllers;

import com.example.toDoList.dto.TaskDto;
import com.example.toDoList.dto.NewTaskDto;
import com.example.toDoList.dto.UpdateTaskDto;
import com.example.toDoList.service.TaskService;
import com.example.toDoList.util.TaskStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.toDoList.util.Constants.PATTERN_DATE;

@RestController
@RequestMapping("/users/{userId}/task")
@Validated
public class TaskController {

    private final static Logger log = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public TaskDto createdTask(@PathVariable(value = "userId") Long userId,
                               @Valid @RequestBody NewTaskDto task) {
        log.info("Creating task {} by user id {}", task, userId);
        return taskService.createdTask(userId, task);
    }

    @GetMapping("/{taskId}")
    public TaskDto getTaskById(@PathVariable(value = "userId") Long userId,
            @PathVariable(value = "taskId") Long taskId) {
        log.info("Get task by userId {} and taskId {}", userId, taskId);
        return taskService.getTaskById(userId, taskId);
    }

    @GetMapping
    public List<TaskDto> getAllTasks(@PathVariable(value = "userId") Long userId,
                                     // список статусов в которых находятся искомые задачи
                                     @RequestParam(required = false) List<TaskStatus> status,
                                     // текст для поиска в содержимом аннотации и подробном описании события
                                     @RequestParam(required = false) String text,
                                     // дата и время не раньше которых должна быть начата задача
                                     @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN_DATE)
                                         LocalDateTime rangeStart,
                                     // дата и время не позже которых должна быть начата задача
                                     @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN_DATE)
                                         LocalDateTime rangeEnd,
                                     @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                     @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Get tasks with parameters: userId {}, status {}, text {}, rangeStart {}, rangeEnd {}, from {}, size {}", userId,
                status, text, rangeStart, rangeEnd, from, size);
        return taskService.getTasksWithParameters(userId, status, text, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{taskId}")
    public TaskDto updateTask(@PathVariable(value = "userId") Long userId,
                              @PathVariable(value = "taskId") Long taskId,
                              @Valid @RequestBody UpdateTaskDto taskDto) {
        log.info("Updating task {} with userId {}, taskId {}", taskDto, userId, taskId);
        return taskService.updateTaskById(taskDto, userId, taskId);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable(value = "userId") Long userId,
                           @PathVariable(value = "taskId") Long taskId) {
        log.info("Deleting task by Id {} userId {}", taskId, userId);
        taskService.deleteTaskById(userId, taskId);
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAllTasks(@PathVariable(value = "userId") Long userId) {
        log.info("Deleting all tasks by userId {}", userId);
        taskService.deleteAllTaskByUserId(userId);
    }
}
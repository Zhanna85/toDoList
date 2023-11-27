package com.example.toDoList.controllers;

import com.example.toDoList.dto.*;
import com.example.toDoList.service.SubtaskService;
import com.example.toDoList.util.TaskStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.toDoList.util.Constants.PATTERN_DATE;

@RestController
@RequestMapping("/users/{userId}/subtask")
public class SubtaskController {

    // получаем логер с именем класса, к которому он относится
    private final static Logger log = LoggerFactory.getLogger(SubtaskController.class);
    private final SubtaskService subtaskService;

    @Autowired
    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubtaskDto createdSubtask(@PathVariable(value = "userId") Long userId,
                                     @RequestParam(value = "taskId") Long taskId,
                                     @Valid @RequestBody NewSubtaskDto newSubtask) {
        log.info("Creating subtask {} by user id {} and task id {}", newSubtask, userId, taskId);
        return subtaskService.createdSubtask(userId, taskId, newSubtask);
    }

    @GetMapping("/{subtaskId}")
    public SubtaskDto getSubtaskById(@PathVariable(value = "userId") Long userId,
                               @PathVariable(value = "subtaskId") Long subtaskId) {
        log.info("Get task by user id {} and subtask id {}", userId, subtaskId);
        return subtaskService.getSubtaskById(userId, subtaskId);
    }

    @GetMapping
    public List<SubtaskDto> getAllSubtasks(@PathVariable(value = "userId") Long userId,
                                     // id task
                                     @RequestParam(value = "taskId") @Positive Long taskId,
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
        log.info("Get tasks with parameters: userId {}, task id {}, status {}, text {}, rangeStart {}, rangeEnd {}, " +
                        "from {}, size {}", userId, taskId, status, text, rangeStart, rangeEnd, from, size);
        return subtaskService.getSubtasksWithParameters(userId, taskId, status, text, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{subtaskId}")
    public SubtaskDto updateSubtask(@PathVariable(value = "userId") Long userId,
                              @PathVariable(value = "subtaskId") Long subtaskId,
                              @Valid @RequestBody UpdateSubtaskDto subtaskDto) {
        log.info("Updating subtask {} with user id {}, subtask id {}", subtaskDto, userId, subtaskId);
        return subtaskService.updateSubtaskById(subtaskDto, userId, subtaskId);
    }

    @DeleteMapping("/{subtaskId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSubtask(@PathVariable(value = "userId") Long userId,
                           @PathVariable(value = "subtaskId") Long subtaskId) {
        log.info("Deleting subtask by id {} user id {}", subtaskId, userId);
        subtaskService.deleteSubtaskById(userId, subtaskId);
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAllSubtasks(@PathVariable(value = "userId") Long userId,
                                  @RequestParam(value = "taskId") Long taskId) {
        log.info("Deleting all subtasks by task id {} and user id {}", taskId, userId);
        subtaskService.deleteAllSubtaskByTaskId(taskId, userId);
    }
}
package com.example.toDoList.service;

import com.example.toDoList.dto.NewSubtaskDto;
import com.example.toDoList.dto.SubtaskDto;
import com.example.toDoList.dto.UpdateSubtaskDto;
import com.example.toDoList.util.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface SubtaskService {
    SubtaskDto createdSubtask(Long userId, Long taskId, NewSubtaskDto newSubtask);

    SubtaskDto getSubtaskById(Long userId, Long subtaskId);

    SubtaskDto updateSubtaskById(UpdateSubtaskDto subtaskDto, Long userId, Long subtaskId);

    void deleteSubtaskById(Long userId, Long subtaskId);

    void deleteAllSubtaskByTaskId(Long taskId, Long userId);

    List<SubtaskDto> getSubtasksWithParameters(Long userId, Long taskId, List<TaskStatus> status, String text, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}
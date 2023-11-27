package com.example.toDoList.mapper;

import com.example.toDoList.dto.NewTaskDto;
import com.example.toDoList.dto.SubtaskShortDto;
import com.example.toDoList.dto.TaskDto;
import com.example.toDoList.model.Task;
import com.example.toDoList.model.User;

import java.util.List;

public class TaskMapper {

    private TaskMapper() {
    }

    public static Task toTask(User user, NewTaskDto dto) {
        Task task = new Task();
        task.setInitiator(user);
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setStartTime(dto.getStartTime());
        return task;
    }

    public static TaskDto toTaskDto(Task task, List<SubtaskShortDto> subtaskDtoList) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setType(task.getType());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setStartTime(task.getStartTime());
        dto.setUserId(task.getInitiator().getId());
        dto.setCreatedOn(task.getCreatedOn());
        dto.setListSubtask(subtaskDtoList);
        return dto;
    }
}
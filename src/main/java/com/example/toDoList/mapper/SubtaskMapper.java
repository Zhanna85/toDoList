package com.example.toDoList.mapper;

import com.example.toDoList.dto.NewSubtaskDto;
import com.example.toDoList.dto.SubtaskDto;
import com.example.toDoList.dto.SubtaskShortDto;
import com.example.toDoList.model.Subtask;
import com.example.toDoList.model.Task;
import com.example.toDoList.model.User;

public class SubtaskMapper {

    private SubtaskMapper() {
    }

    public static SubtaskShortDto toSubtaskShortDto(Subtask subtask) {
        return  new SubtaskShortDto(
                subtask.getId(),
                subtask.getType(),
                subtask.getName(),
                subtask.getDescription(),
                subtask.getStatus(),
                subtask.getStartTime(),
                subtask.getTask().getId()
        );
    }

    public static Subtask toSubtask(User user, Task task, NewSubtaskDto dto) {
        Subtask subtask = new Subtask();
        subtask.setInitiator(user);
        subtask.setTask(task);
        subtask.setName(dto.getName());
        subtask.setDescription(dto.getDescription());
        subtask.setStartTime(dto.getStartTime());
        return subtask;
    }

    public static SubtaskDto toSubtaskDto(Subtask subtask) {
        SubtaskDto subtaskDto = new SubtaskDto();
        subtaskDto.setId(subtask.getId());
        subtaskDto.setType(subtask.getType());
        subtaskDto.setName(subtask.getName());
        subtaskDto.setDescription(subtask.getDescription());
        subtaskDto.setStatus(subtask.getStatus());
        subtaskDto.setStartTime(subtask.getStartTime());
        subtaskDto.setUserId(subtask.getInitiator().getId());
        subtaskDto.setCreatedOn(subtask.getCreatedOn());
        subtaskDto.setTaskId(subtask.getTask().getId());
        return subtaskDto;
    }
}
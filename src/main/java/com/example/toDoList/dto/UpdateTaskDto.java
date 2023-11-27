package com.example.toDoList.dto;

import com.example.toDoList.util.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import static com.example.toDoList.util.Constants.PATTERN_DATE;

public class UpdateTaskDto {

    @Size(min = 10, max = 2000)
    private String name; // Название, кратко описывающее суть задачи (например, «Переезд»).
    @Size(max = 7000)
    private String description; // Полное описание;
    private TaskStatus status; // Статус, отображающий её прогресс.
    @Future
    @JsonFormat(pattern = PATTERN_DATE)
    private LocalDateTime startTime; // Дата и время, когда предполагается приступить к выполнению задачи.

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "UpdateTaskDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                '}';
    }
}
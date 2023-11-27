package com.example.toDoList.dto;

import com.example.toDoList.util.TaskStatus;
import com.example.toDoList.util.TypesTasks;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static com.example.toDoList.util.Constants.PATTERN_DATE;

public class SubtaskShortDto {

    private Long id;  // Идентификатор;
    private TypesTasks type; // Тип задачи.
    private String name; // Название, кратко описывающее суть задачи (например, «Переезд»).
    private String description; // Полное описание;
    private TaskStatus status; // Статус, отображающий её прогресс.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_DATE)
    protected LocalDateTime startTime; // Дата и время, когда предполагается приступить к выполнению задачи.
    private Long taskId; // идентификатор задачи по которой создана подзадача

    public SubtaskShortDto(Long id, TypesTasks type, String name, String description, TaskStatus status,
                           LocalDateTime startTime, Long taskId) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.taskId = taskId;
    }


//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setType(TypesTasks type) {
//        this.type = type;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setStatus(TaskStatus status) {
//        this.status = status;
//    }
//
//    public void setStartTime(LocalDateTime startTime) {
//        this.startTime = startTime;
//    }
    public Long getTaskId() {
        return taskId;
    }

    @Override
    public String toString() {
        return "SubtaskShortDto{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", taskId=" + taskId +
                '}';
    }
}
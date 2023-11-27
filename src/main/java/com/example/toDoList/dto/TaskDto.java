package com.example.toDoList.dto;

import com.example.toDoList.util.TaskStatus;
import com.example.toDoList.util.TypesTasks;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.toDoList.util.Constants.PATTERN_DATE;

public class TaskDto {

    protected Long id;  // Идентификатор;
    protected TypesTasks type; // Тип задачи.
    protected String name; // Название, кратко описывающее суть задачи (например, «Переезд»).
    protected String description; // Полное описание;
    protected TaskStatus status; // Статус, отображающий её прогресс.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_DATE)
    protected LocalDateTime startTime; // Дата и время, когда предполагается приступить к выполнению задачи.
    protected Long userId; // Идентификатор пользователя;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_DATE)
    protected LocalDateTime createdOn; // Дата и время создания
    private List<SubtaskShortDto> listSubtask; // Подзадачи

//    public Long getId() {
//        return id;
//    }
//
//    public TypesTasks getType() {
//        return type;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public TaskStatus getStatus() {
//        return status;
//    }
//
//    public LocalDateTime getStartTime() {
//        return startTime;
//    }
//
//    public LocalDateTime getCreatedOn() {
//        return createdOn;
//    }
//
//    public List<SubtaskDto> getListSubtask() {
//        return listSubtask;
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(TypesTasks type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setListSubtask(List<SubtaskShortDto> listSubtask) {
        this.listSubtask = listSubtask;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", userId=" + userId +
                ", createdOn=" + createdOn +
                ", listSubtask=" + listSubtask +
                '}';
    }
}
package com.example.toDoList.dto;

public class SubtaskDto extends TaskDto {

    private Long taskId; // id задачи, к которой создана подзадача

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "DtoSubtask{" +
                "taskId=" + taskId +
                ", id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", createdOn=" + createdOn +
                '}';
    }
}
package com.example.toDoList.model;

import com.example.toDoList.util.TypesTasks;
import jakarta.persistence.*;

import java.util.Objects;

import static com.example.toDoList.util.TypesTasks.SUBTASK;

@Entity
@Table(name = "subtasks")
public class Subtask extends Task {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task; // задача, к которой создана подзадача

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private final TypesTasks type = SUBTASK; // тип задачи

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TypesTasks getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(task, subtask.task) && type == subtask.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), task, type);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", type=" + type +
                ", taskId=" + task.id +
                ", initiator=" + initiator +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", createdOn=" + createdOn +
                '}';
    }
}
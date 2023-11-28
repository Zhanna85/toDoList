package com.example.toDoList.model;

import com.example.toDoList.util.TaskStatus;
import com.example.toDoList.util.TypesTasks;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.toDoList.util.Constants.PATTERN_DATE;
import static com.example.toDoList.util.TaskStatus.NEW;
import static com.example.toDoList.util.TypesTasks.TASK;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    protected Long id;  // Идентификатор;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private final TypesTasks type = TASK; // Тип задачи.

    @Column(name = "name", nullable = false, length = 2000)
    protected String name; // Название, кратко описывающее суть задачи (например, «Переезд»).

    @Column(name = "description", length = 7000)
    protected String description; // Полное описание;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    protected TaskStatus status = NEW; // Статус, отображающий её прогресс.

    @Column(name = "start_time", nullable = false)
    @DateTimeFormat(pattern = PATTERN_DATE)
    protected LocalDateTime startTime; // Дата и время, когда предполагается приступить к выполнению задачи.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    protected User initiator;

    @Column(name = "created_on", nullable = false)
    @DateTimeFormat(pattern = PATTERN_DATE)
    protected LocalDateTime createdOn = LocalDateTime.now(); // Дата и время создания

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public TypesTasks getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(initiator, task.initiator) && type == task.type && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startTime, task.startTime) && Objects.equals(createdOn, task.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, initiator, type, name, description, status, startTime, createdOn);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", initiator=" + initiator +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", createdOn=" + createdOn +
                '}';
    }
}
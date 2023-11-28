package com.example.toDoList.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;

import static com.example.toDoList.util.Constants.PATTERN_DATE;

public class NewTaskDto {

    @NotBlank
    @Size(min = 10, max = 2000)
    protected String name; // Название, кратко описывающее суть задачи (например, «Переезд»).

    @Size(max = 7000)
    protected String description; // Полное описание;

    @NotBlank
    @Future
    @JsonFormat(pattern = PATTERN_DATE)
    protected LocalDateTime startTime; // Дата и время, когда предполагается приступить к выполнению задачи.

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "NewDtoTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}
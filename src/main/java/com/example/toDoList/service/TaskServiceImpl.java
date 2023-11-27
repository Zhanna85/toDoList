package com.example.toDoList.service;

import com.example.toDoList.dto.NewTaskDto;
import com.example.toDoList.dto.SubtaskShortDto;
import com.example.toDoList.dto.TaskDto;
import com.example.toDoList.dto.UpdateTaskDto;
import com.example.toDoList.exception.NotFoundException;
import com.example.toDoList.exception.ValidateDateException;
import com.example.toDoList.mapper.SubtaskMapper;
import com.example.toDoList.mapper.TaskMapper;
import com.example.toDoList.model.Subtask;
import com.example.toDoList.model.Task;
import com.example.toDoList.model.User;
import com.example.toDoList.repository.SubtaskRepository;
import com.example.toDoList.repository.TaskRepository;
import com.example.toDoList.repository.UserRepository;
import com.example.toDoList.util.PaginationSetup;
import com.example.toDoList.util.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.toDoList.util.Message.*;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final static Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SubtaskRepository subtaskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, SubtaskRepository subtaskRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.subtaskRepository = subtaskRepository;
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + id));
    }

    private List<SubtaskShortDto> getSubtaskList(Long taskId) {
        return subtaskRepository.findAllByTaskId(taskId).stream()
                .map(SubtaskMapper::toSubtaskShortDto)
                .collect(Collectors.toList());
    }

    private Task getTaskByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findByIdAndInitiatorId(taskId, userId)
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + taskId));
    }

    private void validDateParam(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidateDateException("The range start date cannot be is after range end date");
            }
        }
    }

    private LocalDateTime getRangeStart(LocalDateTime rangeStart) {
        // если предполагаемая дата начала события не указана берем за параметр текущую дату
        return Objects.requireNonNullElseGet(rangeStart, LocalDateTime::now);
    }

    private List<Task> getTasksBeforeRangeEnd(List<Task> taskList, LocalDateTime rangeEnd) {
        return taskList.stream().filter(task -> task.getStartTime().isBefore(rangeEnd)).collect(Collectors.toList());
    }

    // формируем мапу с группировкой по ид задач и SubtaskShortDto
    private Map<Long, List<SubtaskShortDto>> getSubtaskByListIdTask(List<Task> listTask) {
        Set<Long> idsTasks = listTask.stream()
                .map(Task::getId)
                .collect(Collectors.toSet());
        return subtaskRepository.findAllByTaskIdIn(idsTasks).stream()
                .map(SubtaskMapper::toSubtaskShortDto)
                .collect(Collectors.groupingBy(SubtaskShortDto::getTaskId));
    }

    @Transactional
    @Override
    public TaskDto createdTask(Long userId, NewTaskDto newTask) {
        User user = getUserById(userId);
        Task task = taskRepository.save(TaskMapper.toTask(user, newTask));
        log.info(ADD_MODEL.getMessage(), task);
        return TaskMapper.toTaskDto(task, new ArrayList<>());
    }

    @Override
    public TaskDto getTaskById(Long userId, Long taskId) {
        Task task = getTaskByIdAndUserId(taskId, userId);
        List<SubtaskShortDto> subtaskList = getSubtaskList(taskId);
        log.info(GET_MODEL_BY_ID.getMessage(), task);
        return TaskMapper.toTaskDto(task, subtaskList);
    }

    @Override
    public List<TaskDto> getTasksWithParameters(Long userId,
                                                List<TaskStatus> status,
                                                String text,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Integer from,
                                                Integer size) {
        log.info(REQUEST_ALL.getMessage());
        validDateParam(rangeStart, rangeEnd); // проверяем даты
        PageRequest pageable = new PaginationSetup(from, size, Sort.unsorted()); // сортировка

        // формируем список задач по параметрам
        List<Task> listTask = taskRepository.findByInitiatorIdWithParameters(userId, status, getRangeStart(rangeStart),
                text, pageable);

        if(rangeEnd != null) {
            listTask = getTasksBeforeRangeEnd(listTask, rangeEnd);
        }

        // формируем мапу, где ключ id задачи, а значение подзадача
        Map<Long, List<SubtaskShortDto>> subtasks = getSubtaskByListIdTask(listTask);

        return listTask.stream()
                .map(task -> TaskMapper.toTaskDto(
                        task,
                        subtasks.getOrDefault(task.getId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TaskDto updateTaskById(UpdateTaskDto taskDto, Long userId, Long taskId) {
        Task task = getTaskByIdAndUserId(taskId, userId);
        String name = taskDto.getName();
        String description = taskDto.getDescription();
        TaskStatus status = taskDto.getStatus();
        LocalDateTime startTime = taskDto.getStartTime();
        List<Subtask> subtasks = subtaskRepository.findAllByTaskId(taskId);

        if (name != null) {
            task.setName(name);
        }
        if (description != null) {
            task.setDescription(description);
        }

        // статус у подзадач меняется, если новый статус DONE
        if (status != null && !status.equals(task.getStatus())) {
            if (subtasks.isEmpty()) {
                task.setStatus(status);
            }
            if (!subtasks.isEmpty() && status.equals(TaskStatus.DONE)) {
                task.setStatus(status);
                subtasks.stream()
                        .filter(subtask -> !subtask.getStatus().equals(status))
                        .forEach(subtask -> subtask.setStatus(status));
                subtaskRepository.saveAll(subtasks);
            }
        }
        if (startTime != null) {
            task.setStartTime(startTime);
        }

        List<SubtaskShortDto> subtaskList = subtasks.stream()
                .map(SubtaskMapper::toSubtaskShortDto)
                .collect(Collectors.toList());
        Task updatedTask = taskRepository.save(task);
        log.info(UPDATED_MODEL.getMessage(), taskId, updatedTask);
        return TaskMapper.toTaskDto(updatedTask, subtaskList);
    }

    @Transactional
    @Override
    public void deleteTaskById(Long userId, Long taskId) {
        log.info(DELETE_MODEL.getMessage(), taskId);
        getTaskByIdAndUserId(taskId, userId);
        subtaskRepository.deleteAllByTaskId(taskId);
        taskRepository.deleteById(taskId);
    }

    @Transactional
    @Override
    public void deleteAllTaskByUserId(Long userId) {
        log.info(DELETE_ALL_MODEL.getMessage(), userId);
        subtaskRepository.deleteAllByInitiatorId(userId);
        taskRepository.deleteAllByInitiatorId(userId);
    }
}
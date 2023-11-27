package com.example.toDoList.service;

import com.example.toDoList.controllers.SubtaskController;
import com.example.toDoList.dto.NewSubtaskDto;
import com.example.toDoList.dto.SubtaskDto;
import com.example.toDoList.dto.UpdateSubtaskDto;
import com.example.toDoList.exception.NotFoundException;
import com.example.toDoList.exception.ValidateDateException;
import com.example.toDoList.exception.ValidateException;
import com.example.toDoList.mapper.SubtaskMapper;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.toDoList.util.Message.*;

@Service
@Transactional(readOnly = true)
public class SubtaskServiceImpl implements SubtaskService{

    private final static Logger log = LoggerFactory.getLogger(SubtaskController.class);
    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public SubtaskServiceImpl(SubtaskRepository subtaskRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + id));
    }

    private Task getTaskByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findByIdAndInitiatorId(taskId, userId)
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + taskId));
    }

    private Subtask getSubtaskByIdAndUserId(Long subtaskId, Long userId) {
        return subtaskRepository.findByIdAndInitiatorId(subtaskId, userId)
                .orElseThrow(() -> new NotFoundException(MODEL_NOT_FOUND.getMessage() + subtaskId));
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

    private List<Subtask> getSubtasksBeforeRangeEnd(List<Subtask> subtasks, LocalDateTime rangeEnd) {
        return subtasks.stream().filter(subtask -> subtask.getStartTime().isBefore(rangeEnd)).collect(Collectors.toList());
    }

    @Override
    public SubtaskDto createdSubtask(Long userId, Long taskId, NewSubtaskDto newSubtask) {
        User user = getUserById(userId);
        Task task = getTaskByIdAndUserId(taskId, userId);
        Subtask subtask = subtaskRepository.save(SubtaskMapper.toSubtask(user, task, newSubtask));
        log.info(ADD_MODEL.getMessage(), subtask);
        return SubtaskMapper.toSubtaskDto(subtask);
    }

    @Override
    public SubtaskDto getSubtaskById(Long userId, Long subtaskId) {
        Subtask subtask = getSubtaskByIdAndUserId(subtaskId, userId);
        log.info(GET_MODEL_BY_ID.getMessage(), subtask);
        return SubtaskMapper.toSubtaskDto(subtask);
    }

    @Override
    public SubtaskDto updateSubtaskById(UpdateSubtaskDto subtaskDto, Long userId, Long subtaskId) {
        Subtask subtask = getSubtaskByIdAndUserId(subtaskId, userId);
        TaskStatus statusTask = taskRepository.getTaskStatusById(subtask.getTask().getId());
        String name = subtaskDto.getName();
        String description = subtaskDto.getDescription();
        TaskStatus status = subtaskDto.getStatus();
        LocalDateTime startTime = subtaskDto.getStartTime();

        if (name != null) {
            subtask.setName(name);
        }
        if (description != null) {
            subtask.setDescription(description);
        }
        if (status != null && !status.equals(subtask.getStatus())) {
            if (statusTask.equals(TaskStatus.DONE)) {
                throw new ValidateException("The status of the task is \"Done\" status subtask update is not possible.");
            } else {
                subtask.setStatus(status);
            }
        }
        if (startTime != null) {
            subtask.setStartTime(startTime);
        }

        Subtask updateSubtask = subtaskRepository.save(subtask);
        log.info(UPDATED_MODEL.getMessage(), subtaskId, subtaskDto);
        return SubtaskMapper.toSubtaskDto(subtask);
    }

    @Override
    public void deleteSubtaskById(Long userId, Long subtaskId) {
        log.info(DELETE_MODEL.getMessage(), subtaskId);
        getSubtaskByIdAndUserId(subtaskId, userId);
        subtaskRepository.deleteById(subtaskId);
    }

    @Override
    public void deleteAllSubtaskByTaskId(Long taskId, Long userId) {
        log.info(DELETE_ALL_MODEL_SUB.getMessage(), userId, taskId);
        getTaskByIdAndUserId(taskId,userId);
        subtaskRepository.deleteAllByTaskId(taskId);
    }

    @Override
    public List<SubtaskDto> getSubtasksWithParameters(Long userId,
                                                      Long taskId,
                                                      List<TaskStatus> status,
                                                      String text,
                                                      LocalDateTime rangeStart,
                                                      LocalDateTime rangeEnd,
                                                      Integer from,
                                                      Integer size) {
        log.info(REQUEST_ALL.getMessage());
        validDateParam(rangeStart, rangeEnd); // проверяем даты
        PageRequest pageable = new PaginationSetup(from, size, Sort.unsorted()); // сортировка
        getTaskByIdAndUserId(taskId,userId);
        // формируем список задач по параметрам
        List<Subtask> subtasks = subtaskRepository.findAllByTaskIdWithParameters(userId, taskId, status, text, rangeStart,
                                 pageable);
        if(rangeEnd != null) {
            subtasks = getSubtasksBeforeRangeEnd(subtasks, rangeEnd);
        }
        return subtasks.stream()
                .map(SubtaskMapper::toSubtaskDto)
                .collect(Collectors.toList());
    }
}
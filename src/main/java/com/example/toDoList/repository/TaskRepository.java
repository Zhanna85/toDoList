package com.example.toDoList.repository;

import com.example.toDoList.model.Task;
import com.example.toDoList.util.TaskStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndInitiatorId(Long taskId, Long userId);

    void deleteAllByInitiatorId(Long userId);

    @Query(
            "select t " +
            "from Task AS t " +
                    "where t.initiator.id = :userId " +
                    "and t.startTime > :rangeStart " +
                    "and (:status is null or t.status in :status) " +
                    "and (:text is null or (upper(t.name) like upper(concat('%', :text, '%'))) " +
                    "or (upper(t.description) like upper(concat('%', :text, '%'))))"
    )
    List<Task> findByInitiatorIdWithParameters(Long userId, List<TaskStatus> status, LocalDateTime rangeStart,
                                               String text, PageRequest pageable);

    @Query(
            "select t.status " +
                    "from Task AS t " +
                    "where t.id = :id"
    )
    TaskStatus getTaskStatusById(Long id);
}

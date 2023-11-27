package com.example.toDoList.repository;

import com.example.toDoList.model.Subtask;
import com.example.toDoList.util.TaskStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findAllByTaskId(Long taskId);

    void deleteAllByTaskId(Long taskId);

    void deleteAllByInitiatorId(Long userId);

    List<Subtask> findAllByTaskIdIn(Set<Long> idsTasks);

    boolean existsByTaskId(Long taskId);

    Optional<Subtask> findByIdAndInitiatorId(Long subtaskId, Long userId);

    @Query("select t " +
            "from Subtask AS t " +
            "where t.initiator.id = :userId " +
            "and t.task.id = :taskId " +
            "and t.startTime > :rangeStart " +
            "and (:status is null or t.status in :status) " +
            "and (:text is null or (upper(t.name) like upper(concat('%', :text, '%'))) " +
            "or (upper(t.description) like upper(concat('%', :text, '%'))))"
    )
    List<Subtask> findAllByTaskIdWithParameters(Long userId, Long taskId, List<TaskStatus> status, String text,
                                                LocalDateTime rangeStart, PageRequest pageable);
}
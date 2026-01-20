package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.Task;
import com.dayanchor.dayanchor.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);
}

package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}

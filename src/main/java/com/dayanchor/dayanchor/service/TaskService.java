package com.dayanchor.dayanchor.service;

import com.dayanchor.dayanchor.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.dayanchor.dayanchor.entity.Task;
import com.dayanchor.dayanchor.entity.TaskStatus;
import com.dayanchor.dayanchor.entity.EnergyLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(
            String title,
            String description,
            LocalDate taskDate,
            EnergyLevel energyLevel,
            Long userId
    ) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }

        if (taskDate == null) {
            throw new IllegalArgumentException("Task date is required");
        }

        if (energyLevel == null) {
            throw new IllegalArgumentException("Energy level is required");
        }

        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setTaskDate(taskDate);
        task.setEnergyLevel(energyLevel);
        task.setUserId(userId);
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }
}

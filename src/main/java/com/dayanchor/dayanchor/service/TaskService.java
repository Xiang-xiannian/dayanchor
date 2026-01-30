package com.dayanchor.dayanchor.service;

import com.dayanchor.dayanchor.dto.UpdateTaskRequest;
import com.dayanchor.dayanchor.entity.EnergyLevel;
import com.dayanchor.dayanchor.entity.Task;
import com.dayanchor.dayanchor.entity.TaskStatus;
import com.dayanchor.dayanchor.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** Task service. 任务服务 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /** Create task. 创建任务 */
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

    /** List pending tasks. 查询待办任务 */
    public List<Task> getActiveTasks(Long userId) {
        return taskRepository.findByUserIdAndStatus(
                userId,
                TaskStatus.PENDING
        );
    }

    /** Complete task. 完成任务 */
    @Transactional
    public Task completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.COMPLETED);
        return taskRepository.save(task);
    }

    /** Delete task. 删除任务 */
    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found");
        }

        taskRepository.deleteById(taskId);
    }

    /** Update task. 更新任务 */
    public Task updateTask(Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getEnergyLevel() != null) {
            task.setEnergyLevel(request.getEnergyLevel());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        return taskRepository.save(task);
    }
}


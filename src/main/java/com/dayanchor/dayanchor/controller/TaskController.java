package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.dto.CreateTaskRequest;
import com.dayanchor.dayanchor.entity.Task;
import com.dayanchor.dayanchor.service.TaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task createTask(@RequestBody CreateTaskRequest request) {
        return taskService.createTask(
                request.getTitle(),
                request.getDescription(),
                request.getTaskDate(),
                request.getEnergyLevel(),
                request.getUserId()
        );
    }
}

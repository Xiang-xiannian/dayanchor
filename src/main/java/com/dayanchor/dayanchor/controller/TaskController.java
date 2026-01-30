package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.dto.CreateTaskRequest;
import com.dayanchor.dayanchor.dto.UpdateTaskRequest;
import com.dayanchor.dayanchor.entity.Task;
import com.dayanchor.dayanchor.service.TaskService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Task APIs. 任务接口 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /** Create task. 创建任务 */
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

    /** List pending tasks. 查询待办任务 */
    @GetMapping
    public List<Task> getTasks(@RequestParam Long userId) {
        return taskService.getActiveTasks(userId);
    }

    /** Complete task. 完成任务 */
    @PatchMapping("/{id}/complete")
    public Task completeTask(@PathVariable Long id) {
        return taskService.completeTask(id);
    }

    /** Delete task. 删除任务 */
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    /** Update task. 更新任务 */
    @PatchMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(id, request);
    }
}


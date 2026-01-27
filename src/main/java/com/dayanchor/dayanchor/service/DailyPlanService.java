package com.dayanchor.dayanchor.service;

import com.dayanchor.dayanchor.entity.DailyTaskLog;
import com.dayanchor.dayanchor.entity.Task;
import com.dayanchor.dayanchor.entity.TaskStatus;
import com.dayanchor.dayanchor.repository.DailyTaskLogRepository;
import com.dayanchor.dayanchor.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyPlanService {

    private final DailyTaskLogRepository logRepository;
    private final TaskRepository taskRepository;

    public DailyPlanService(DailyTaskLogRepository logRepository, TaskRepository taskRepository) {
        this.logRepository = logRepository;
        this.taskRepository = taskRepository;
    }

    public DailyPlanResponse getDailyPlan(Long userId, LocalDate date) {
        List<DailyTaskLog> dailyLogs = logRepository.findByUserIdAndTaskDate(userId, date);
        List<Task> pendingTasks = taskRepository.findByUserIdAndStatus(userId, TaskStatus.PENDING);

        return new DailyPlanResponse(dailyLogs, pendingTasks);
    }

    public static class DailyPlanResponse {
        private List<DailyTaskLog> dailyLogs;
        private List<Task> pendingTasks;

        public DailyPlanResponse(List<DailyTaskLog> dailyLogs, List<Task> pendingTasks) {
            this.dailyLogs = dailyLogs;
            this.pendingTasks = pendingTasks;
        }

        public List<DailyTaskLog> getDailyLogs() {
            return dailyLogs;
        }

        public void setDailyLogs(List<DailyTaskLog> dailyLogs) {
            this.dailyLogs = dailyLogs;
        }

        public List<Task> getPendingTasks() {
            return pendingTasks;
        }

        public void setPendingTasks(List<Task> pendingTasks) {
            this.pendingTasks = pendingTasks;
        }
    }
}

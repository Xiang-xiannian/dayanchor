package com.dayanchor.dayanchor.service;

import com.dayanchor.dayanchor.entity.DailyTaskLog;
import com.dayanchor.dayanchor.entity.Task;
import com.dayanchor.dayanchor.entity.TaskStatus;
import com.dayanchor.dayanchor.repository.DailyTaskLogRepository;
import com.dayanchor.dayanchor.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.dayanchor.dayanchor.dto.DailyPlanResponse;

import java.time.LocalDate;
import java.util.List;

/** Daily plan service. 每日计划服务 */
@Service
public class DailyPlanService {

    private final DailyTaskLogRepository logRepository;
    private final TaskRepository taskRepository;

    public DailyPlanService(DailyTaskLogRepository logRepository, TaskRepository taskRepository) {
        this.logRepository = logRepository;
        this.taskRepository = taskRepository;
    }

    /** Get daily plan. 获取每日计划 */
    public DailyPlanResponse getDailyPlan(Long userId, LocalDate date) {
        List<DailyTaskLog> dailyLogs = logRepository.findByUserIdAndTaskDate(userId, date);
        List<Task> pendingTasks = taskRepository.findByUserIdAndStatus(userId, TaskStatus.PENDING);
        return new DailyPlanResponse(dailyLogs, pendingTasks);
    }

    /** Get completed count for the day. 获取当天已完成数量（日志+任务） */
    public long getCompletedCount(Long userId, LocalDate date) {
        long completedLogs = logRepository.countByUserIdAndTaskDateAndStatus(userId, date, TaskStatus.COMPLETED);
        long completedTasks = taskRepository.countByUserIdAndTaskDateAndStatus(userId, date, TaskStatus.COMPLETED);
        return completedLogs + completedTasks;
    }

}

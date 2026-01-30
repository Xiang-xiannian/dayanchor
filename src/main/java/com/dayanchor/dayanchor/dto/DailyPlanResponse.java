package com.dayanchor.dayanchor.dto;

import com.dayanchor.dayanchor.entity.DailyTaskLog;
import com.dayanchor.dayanchor.entity.Task;

import java.util.List;

/** Daily plan response DTO. 每日计划响应 DTO */

public class DailyPlanResponse {
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

package com.dayanchor.dayanchor.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyTaskLogScheduler {

    private final DailyTaskLogService dailyTaskLogService;

    public DailyTaskLogScheduler(DailyTaskLogService dailyTaskLogService) {
        this.dailyTaskLogService = dailyTaskLogService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void generateDailyLogsAtMidnight() {
        dailyTaskLogService.generateTodayLogs();
    }
}

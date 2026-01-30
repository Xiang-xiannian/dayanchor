package com.dayanchor.dayanchor.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Daily log scheduler. 每日日志定时任务 */
@Component
public class DailyTaskLogScheduler {

    private final DailyTaskLogService dailyTaskLogService;

    public DailyTaskLogScheduler(DailyTaskLogService dailyTaskLogService) {
        this.dailyTaskLogService = dailyTaskLogService;
    }

    /** Trigger log generation. 触发生成日志 */
    @Scheduled(cron = "0 * * * * *")
    public void generateDailyLogsAtMidnight() {
        dailyTaskLogService.generateTodayLogs();
    }
}


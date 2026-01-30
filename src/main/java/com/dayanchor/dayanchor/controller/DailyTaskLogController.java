package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.entity.DailyTaskLog;
import com.dayanchor.dayanchor.service.DailyTaskLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/** Daily task log APIs. 每日任务日志接口 */
@RestController
@RequestMapping("/api/daily-task-logs")
public class DailyTaskLogController {

    private final DailyTaskLogService dailyTaskLogService;

    public DailyTaskLogController(DailyTaskLogService dailyTaskLogService) {
        this.dailyTaskLogService = dailyTaskLogService;
    }

    /** Get logs by date. 按日期获取日志 */
    @GetMapping
    public List<DailyTaskLog> getLogs(@RequestParam Long userId, @RequestParam LocalDate date) {
        return dailyTaskLogService.getLogsByDate(userId, date);
    }

    /** Mark a log completed. 标记日志为完成 */
    @PatchMapping("/{id}/complete")
    public DailyTaskLog completeLog(@PathVariable Long id) {
        return dailyTaskLogService.completeLog(id);
    }
}


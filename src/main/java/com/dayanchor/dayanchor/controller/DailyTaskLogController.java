package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.entity.DailyTaskLog;
import com.dayanchor.dayanchor.service.DailyTaskLogService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/daily-task-logs")
public class DailyTaskLogController {

    private final DailyTaskLogService dailyTaskLogService;

    public DailyTaskLogController(DailyTaskLogService dailyTaskLogService) {
        this.dailyTaskLogService = dailyTaskLogService;
    }

    @GetMapping
    public List<DailyTaskLog> getLogs(
            @RequestParam Long userId,
            @RequestParam LocalDate date) {
        return dailyTaskLogService.getLogsByDate(userId, date);
    }

    @PatchMapping("/{id}/complete")
    public DailyTaskLog completeLog(@PathVariable Long id) {
        return dailyTaskLogService.completeLog(id);
    }
}

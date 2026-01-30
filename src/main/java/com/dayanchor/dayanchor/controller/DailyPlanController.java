package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.service.DailyPlanService;
import com.dayanchor.dayanchor.dto.DailyPlanResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/** Daily plan APIs. 每日计划接口 */
@RestController
@RequestMapping("/api/daily-plan")
public class DailyPlanController {

    private final DailyPlanService dailyPlanService;

    public DailyPlanController(DailyPlanService dailyPlanService) {
        this.dailyPlanService = dailyPlanService;
    }

    /** Get daily plan. 获取每日计划 */
    @GetMapping
    public DailyPlanResponse getDailyPlan(@RequestParam Long userId, @RequestParam LocalDate date) {
        return dailyPlanService.getDailyPlan(userId, date);
    }

    /** Get completed count for the day. 获取当天已完成数量（日志+任务） */
    @GetMapping("/completed-count")
    public long getCompletedCount(@RequestParam Long userId, @RequestParam LocalDate date) {
        return dailyPlanService.getCompletedCount(userId, date);
    }
}

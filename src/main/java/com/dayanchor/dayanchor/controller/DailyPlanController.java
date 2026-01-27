package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.service.DailyPlanService;
import com.dayanchor.dayanchor.service.DailyPlanService.DailyPlanResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/daily-plan")
public class DailyPlanController {

    private final DailyPlanService dailyPlanService;

    public DailyPlanController(DailyPlanService dailyPlanService) {
        this.dailyPlanService = dailyPlanService;
    }

    @GetMapping
    public DailyPlanResponse getDailyPlan(
            @RequestParam Long userId,
            @RequestParam LocalDate date) {
        return dailyPlanService.getDailyPlan(userId, date);
    }
}

package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.dto.WorkTimerStatusResponse;
import com.dayanchor.dayanchor.service.WorkTimerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/** Work timer APIs. 工作时长计时器接口 */
@RestController
@RequestMapping("/api/work-timer")
public class WorkTimerController {

    private final WorkTimerService workTimerService;

    public WorkTimerController(WorkTimerService workTimerService) {
        this.workTimerService = workTimerService;
    }

    /** Get today's timer status. 获取今天计时状态 */
    @GetMapping("/today")
    public WorkTimerStatusResponse getToday(@RequestParam Long userId) {
        return workTimerService.getTodayStatus(userId);
    }

    /** Start today's timer. 开始今天计时 */
    @PostMapping("/today/start")
    public WorkTimerStatusResponse startToday(@RequestParam Long userId) {
        return workTimerService.startToday(userId);
    }

    /** Pause today's timer. 暂停今天计时 */
    @PostMapping("/today/pause")
    public WorkTimerStatusResponse pauseToday(@RequestParam Long userId) {
        return workTimerService.pauseToday(userId);
    }

    /** Get timer by date. 按日期获取计时状态 */
    @GetMapping("/by-date")
    public WorkTimerStatusResponse getByDate(@RequestParam Long userId, @RequestParam LocalDate date) {
        return workTimerService.getStatusByDate(userId, date);
    }

}

package com.dayanchor.dayanchor.service;

import com.dayanchor.dayanchor.dto.WorkTimerStatusResponse;
import com.dayanchor.dayanchor.entity.DailyWorkTimer;
import com.dayanchor.dayanchor.repository.DailyWorkTimerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class WorkTimerService {

    private final DailyWorkTimerRepository workTimerRepository;

    public WorkTimerService(DailyWorkTimerRepository workTimerRepository) {
        this.workTimerRepository = workTimerRepository;
    }

    /** Get today's status. 获取今天的计时状态 */
    public WorkTimerStatusResponse getTodayStatus(Long userId) {
        validateUserId(userId);

        LocalDate today = LocalDate.now();
        return workTimerRepository.findByUserIdAndWorkDate(userId, today)
                .map(timer -> toResponse(timer, LocalDateTime.now()))
                .orElseGet(() -> new WorkTimerStatusResponse(today, 0L, false, null));
    }

    /** Start today's timer. 开始今天计时 */
    @Transactional
    public WorkTimerStatusResponse startToday(Long userId) {
        validateUserId(userId);

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        DailyWorkTimer timer = workTimerRepository.findByUserIdAndWorkDate(userId, today)
                .orElseGet(() -> createNewTimer(userId, today, now));

        if (timer.getRunningStartAt() == null) {
            timer.setRunningStartAt(now);
            workTimerRepository.save(timer);
        }

        return toResponse(timer, now);
    }

    /** Pause today's timer. 暂停今天计时 */
    @Transactional
    public WorkTimerStatusResponse pauseToday(Long userId) {
        validateUserId(userId);

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        DailyWorkTimer timer = workTimerRepository.findByUserIdAndWorkDate(userId, today)
                .orElseGet(() -> createNewTimer(userId, today, now));

        if (timer.getRunningStartAt() != null) {
            long extraSeconds = Duration.between(timer.getRunningStartAt(), now).getSeconds();
            if (extraSeconds > 0) {
                timer.setTotalSeconds(timer.getTotalSeconds() + extraSeconds);
            }
            timer.setRunningStartAt(null);
            workTimerRepository.save(timer);
        }

        return toResponse(timer, now);
    }

    /** Get status by date. 按日期获取计时状态 */
    public WorkTimerStatusResponse getStatusByDate(Long userId, LocalDate date) {
        validateUserId(userId);
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }

        return workTimerRepository.findByUserIdAndWorkDate(userId, date)
                .map(timer -> toResponse(timer, LocalDateTime.now()))
                .orElseGet(() -> new WorkTimerStatusResponse(date, 0L, false, null));
    }

    /** Create empty timer for today. 创建今天的空计时记录 */
    private DailyWorkTimer createNewTimer(Long userId, LocalDate date, LocalDateTime now) {
        DailyWorkTimer timer = new DailyWorkTimer();
        timer.setUserId(userId);
        timer.setWorkDate(date);
        timer.setTotalSeconds(0L);
        timer.setRunningStartAt(null);
        timer.setCreatedAt(now);
        return workTimerRepository.save(timer);
    }

    /** Build response with running time included. 生成包含运行中时间的响应 */
    private WorkTimerStatusResponse toResponse(DailyWorkTimer timer, LocalDateTime now) {
        long total = timer.getTotalSeconds() == null ? 0L : timer.getTotalSeconds();
        boolean running = timer.getRunningStartAt() != null;

        if (running) {
            long extraSeconds = Duration.between(timer.getRunningStartAt(), now).getSeconds();
            if (extraSeconds > 0) {
                total += extraSeconds;
            }
        }

        return new WorkTimerStatusResponse(
                timer.getWorkDate(),
                total,
                running,
                timer.getRunningStartAt());
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }
    }
}

package com.dayanchor.dayanchor.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WorkTimerStatusResponse {

    private LocalDate date;
    private long totalSeconds;
    private boolean running;
    private LocalDateTime runningStartAt;

    public WorkTimerStatusResponse(
            LocalDate date,
            long totalSeconds,
            boolean running,
            LocalDateTime runningStartAt) {
        this.date = date;
        this.totalSeconds = totalSeconds;
        this.running = running;
        this.runningStartAt = runningStartAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(long totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public LocalDateTime getRunningStartAt() {
        return runningStartAt;
    }

    public void setRunningStartAt(LocalDateTime runningStartAt) {
        this.runningStartAt = runningStartAt;
    }
}

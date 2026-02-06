package com.dayanchor.dayanchor.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_work_timers", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "work_date" }))
public class DailyWorkTimer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "total_seconds", nullable = false)
    private Long totalSeconds;

    @Column(name = "running_start_at")
    private LocalDateTime runningStartAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public Long getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(Long totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public LocalDateTime getRunningStartAt() {
        return runningStartAt;
    }

    public void setRunningStartAt(LocalDateTime runningStartAt) {
        this.runningStartAt = runningStartAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

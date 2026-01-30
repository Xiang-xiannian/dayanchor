package com.dayanchor.dayanchor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

/** 宝箱开启记录：每天最多一次的留痕 */
@Entity
@Table(name = "chest_open_logs", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "open_date" }))
public class ChestOpenLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;

    @Column(name = "reward_item_id", nullable = false)
    private Long rewardItemId;

    @Column(nullable = false)
    private LocalDateTime openedAt;

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

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public void setRewardItemId(Long rewardItemId) {
        this.rewardItemId = rewardItemId;
    }

    public Long getRewardItemId() {
        return rewardItemId;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }
}

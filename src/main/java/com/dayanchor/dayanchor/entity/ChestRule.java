package com.dayanchor.dayanchor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** 宝箱规则：用户设置完成 n 项后可开宝箱 */
@Entity
@Table(name = "chest_rules")
public class ChestRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    /** 触发宝箱的完成数量 n */
    @Column(nullable = false)
    private int requiredCompletedCount;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

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

    public int getRequiredCompletedCount() {
        return requiredCompletedCount;
    }

    public void setRequiredCompletedCount(int requiredCompletedCount) {
        this.requiredCompletedCount = requiredCompletedCount;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

package com.dayanchor.dayanchor.dto;

public class SetChestRuleRequest {
    private Long userId;
    private int requiredCompletedCount;

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
}

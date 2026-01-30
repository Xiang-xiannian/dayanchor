package com.dayanchor.dayanchor.dto;

import com.dayanchor.dayanchor.entity.RewardItem;

public class OpenChestResponse {
    private boolean opened;
    private String message;
    private RewardItem reward;
    private long completedCount;
    private int requiredCount;

    public boolean getOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RewardItem getReward() {
        return reward;
    }

    public void setReward(RewardItem reward) {
        this.reward = reward;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
    }

    public int getRequiredCount() {
        return requiredCount;
    }

    public void setRequiredCount(int requiredCount) {
        this.requiredCount = requiredCount;
    }
}

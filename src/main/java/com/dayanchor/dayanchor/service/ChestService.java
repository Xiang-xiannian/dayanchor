package com.dayanchor.dayanchor.service;

import org.springframework.stereotype.Service;
import com.dayanchor.dayanchor.entity.ChestOpenLog;
import com.dayanchor.dayanchor.entity.ChestRule;
import com.dayanchor.dayanchor.entity.RewardItem;
import com.dayanchor.dayanchor.repository.ChestOpenLogRepository;
import com.dayanchor.dayanchor.repository.ChestRuleRepository;
import com.dayanchor.dayanchor.repository.RewardItemRepository;
import com.dayanchor.dayanchor.dto.OpenChestResponse;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ChestService {
    private final RewardItemRepository rewardItemRepository;
    private final ChestRuleRepository chestRuleRepository;
    private final ChestOpenLogRepository chestOpenLogRepository;
    private final DailyPlanService dailyPlanService;

    public ChestService(
            RewardItemRepository rewardItemRepository,
            ChestRuleRepository chestRuleRepository,
            ChestOpenLogRepository chestOpenLogRepository,
            DailyPlanService dailyPlanService) {
        this.rewardItemRepository = rewardItemRepository;
        this.chestRuleRepository = chestRuleRepository;
        this.chestOpenLogRepository = chestOpenLogRepository;
        this.dailyPlanService = dailyPlanService;
    }

    /** Try open chest. 尝试开启宝箱 */
    @Transactional
    public OpenChestResponse tryOpenChest(Long userId, LocalDate date) {
        // 1) 奖励池检查（>=1 才启用）
        List<RewardItem> rewards = rewardItemRepository.findByUserIdAndEnabledTrue(userId);
        if (rewards.isEmpty())
            return response(false, "请先添加奖励事项", null, 0, 0);

        // 2) 规则检查（n 是否设置）
        ChestRule rule = chestRuleRepository.findByUserId(userId).orElse(null);
        if (rule == null)
            return response(false, "请先设置宝箱规则", null, 0, 0);

        // 3) 今日是否已开
        if (chestOpenLogRepository.existsByUserIdAndOpenDate(userId, date))
            return response(false, "今天已开启过宝箱", null, 0, rule.getRequiredCompletedCount());

        // 4) 今日完成数是否达标
        long completed = dailyPlanService.getCompletedCount(userId, date);
        int required = rule.getRequiredCompletedCount();
        if (completed < required)
            return response(false, "未达成条件，还差 " + (required - completed) + " 项", null, completed, required);

        // 5) 随机抽奖
        int idx = ThreadLocalRandom.current().nextInt(rewards.size());
        RewardItem reward = rewards.get(idx);

        // 5)6写日志
        ChestOpenLog log = new ChestOpenLog();
        log.setUserId(userId);
        log.setOpenDate(date);
        log.setRewardItemId(reward.getId());
        log.setOpenedAt(LocalDateTime.now());
        chestOpenLogRepository.save(log);

        return response(true, "开启成功", reward, completed, required);
    }

    /** Build response. 组装响应 */
    private OpenChestResponse response(boolean opened, String message, RewardItem reward, long completedCount,
            int requiredCount) {
        OpenChestResponse response = new OpenChestResponse();
        response.setOpened(opened);
        response.setMessage(message);
        response.setReward(reward);
        response.setCompletedCount(completedCount);
        response.setRequiredCount(requiredCount);
        return response;
    }
}

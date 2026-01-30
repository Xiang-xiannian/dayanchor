package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.dto.OpenChestResponse;
import com.dayanchor.dayanchor.dto.SetChestRuleRequest;
import com.dayanchor.dayanchor.entity.ChestRule;
import com.dayanchor.dayanchor.repository.ChestRuleRepository;
import com.dayanchor.dayanchor.service.ChestService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/chest")

public class ChestController {
    private final ChestService chestService;
    private final ChestRuleRepository chestRuleRepository;

    public ChestController(ChestService chestService, ChestRuleRepository chestRuleRepository) {
        this.chestService = chestService;
        this.chestRuleRepository = chestRuleRepository;
    }

    /** Set chest rule. 设置宝箱规则 */
    @PutMapping("/rule")
    public ChestRule setRule(@RequestBody SetChestRuleRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User id is required");
        }
        if (request.getRequiredCompletedCount() <= 0) {
            throw new IllegalArgumentException("requiredCompletedCount must be >= 1");
        }

        ChestRule rule = chestRuleRepository.findByUserId(request.getUserId()).orElse(null);
        if (rule == null) {
            rule = new ChestRule();
            rule.setUserId(request.getUserId());
        }
        rule.setRequiredCompletedCount(request.getRequiredCompletedCount());
        rule.setUpdatedAt(LocalDateTime.now());
        return chestRuleRepository.save(rule);
    }

    /** Open chest. 开宝箱 */
    @PostMapping("/open")
    public OpenChestResponse open(@RequestParam Long userId,
            @RequestParam(required = false) LocalDate date) {
        LocalDate openDate = (date == null) ? LocalDate.now() : date;
        return chestService.tryOpenChest(userId, openDate);
    }
}

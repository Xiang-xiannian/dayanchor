package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.dto.CreateRewardItemRequest;
import com.dayanchor.dayanchor.dto.UpdateRewardItemRequest;
import com.dayanchor.dayanchor.entity.RewardItem;
import org.springframework.web.bind.annotation.*;
import com.dayanchor.dayanchor.repository.RewardItemRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reward-items")
public class RewardItemController {
    private final RewardItemRepository rewardItemRepository;

    public RewardItemController(RewardItemRepository rewardItemRepository) {
        this.rewardItemRepository = rewardItemRepository;
    }

    /** Create reward item. 创建奖励事项 */
    @PostMapping
    public RewardItem create(@RequestBody CreateRewardItemRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User id is required");
        }
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        RewardItem item = new RewardItem();
        item.setUserId(request.getUserId());
        item.setTitle(request.getTitle());
        item.setEnabled(request.getEnabled() == null || request.getEnabled());
        item.setCreatedAt(LocalDateTime.now());
        return rewardItemRepository.save(item);
    }

    /** List reward items by user. 查询奖励事项 */
    @GetMapping
    public List<RewardItem> list(@RequestParam Long userId) {
        return rewardItemRepository.findByUserId(userId);
    }

    /** Update reward item. 更新奖励事项 */
    @PatchMapping("/{id}")
    public RewardItem update(@PathVariable Long id, @RequestBody UpdateRewardItemRequest request) {
        RewardItem item = rewardItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward item not found"));

        if (request.getTitle() != null) {
            item.setTitle(request.getTitle());
        }
        if (request.getEnabled() != null) {
            item.setEnabled(request.getEnabled());
        }
        return rewardItemRepository.save(item);
    }

    /** Delete reward item. 删除奖励事项 */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (!rewardItemRepository.existsById(id)) {
            throw new RuntimeException("Reward item not found");
        }
        rewardItemRepository.deleteById(id);
    }
}

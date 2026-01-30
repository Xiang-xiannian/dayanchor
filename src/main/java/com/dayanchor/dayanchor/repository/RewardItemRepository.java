package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.RewardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RewardItemRepository extends JpaRepository<RewardItem, Long> {
    /** List enabled reward items by user. 按用户查询可用奖励事项 */
    List<RewardItem> findByUserIdAndEnabledTrue(Long userId);

    List<RewardItem> findByUserId(Long userId);

}

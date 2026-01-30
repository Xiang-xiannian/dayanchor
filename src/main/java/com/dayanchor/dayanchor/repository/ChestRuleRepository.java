package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.ChestRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChestRuleRepository extends JpaRepository<ChestRule, Long> {
    /** Find rule by user. 按用户查询宝箱规则 */
    Optional<ChestRule> findByUserId(Long userId);
}

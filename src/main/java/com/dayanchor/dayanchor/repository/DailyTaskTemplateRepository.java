package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.DailyTaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** DailyTaskTemplate DAO. 每日模板 DAO */
public interface DailyTaskTemplateRepository extends JpaRepository<DailyTaskTemplate, Long> {

    /** List active templates by user. 按用户查询启用模板 */
    List<DailyTaskTemplate> findByUserIdAndActiveTrue(Long userId);
}


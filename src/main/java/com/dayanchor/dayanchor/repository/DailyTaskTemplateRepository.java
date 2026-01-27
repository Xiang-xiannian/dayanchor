package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.DailyTaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyTaskTemplateRepository extends JpaRepository<DailyTaskTemplate, Long> {
    List<DailyTaskTemplate> findByUserIdAndActiveTrue(Long userId);
}

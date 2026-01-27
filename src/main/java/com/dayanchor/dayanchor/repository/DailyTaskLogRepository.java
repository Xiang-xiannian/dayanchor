package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.DailyTaskLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyTaskLogRepository extends JpaRepository<DailyTaskLog, Long> {
    List<DailyTaskLog> findByUserIdAndTaskDate(Long userId, LocalDate taskDate);

    boolean existsByTemplateIdAndTaskDate(Long templateId, LocalDate taskDate);
}

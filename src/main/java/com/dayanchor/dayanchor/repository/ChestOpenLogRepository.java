package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.ChestOpenLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ChestOpenLogRepository extends JpaRepository<ChestOpenLog, Long> {
    /** Check if opened today. 判断当天是否已开启 */
    boolean existsByUserIdAndOpenDate(Long userId, LocalDate openDate);
}

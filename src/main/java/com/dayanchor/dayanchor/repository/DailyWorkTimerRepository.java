package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.DailyWorkTimer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyWorkTimerRepository extends JpaRepository<DailyWorkTimer, Long> {

    /** Find timer by user + date. 按用户+日期查询计时 */
    Optional<DailyWorkTimer> findByUserIdAndWorkDate(Long userId, LocalDate workDate);
}

package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.DailyTaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dayanchor.dayanchor.entity.TaskStatus;
import java.time.LocalDate;
import java.util.List;

/** DailyTaskLog DAO. 每日任务日志 DAO */
public interface DailyTaskLogRepository extends JpaRepository<DailyTaskLog, Long> {

    /** Find logs by userId + date. 按用户+日期查询日志 */
    List<DailyTaskLog> findByUserIdAndTaskDate(Long userId, LocalDate taskDate);

    /** Check if template log exists for date. 判断模板在当天是否已生成日志 */
    boolean existsByTemplateIdAndTaskDate(Long templateId, LocalDate taskDate);

    /** Count completed logs by userId + date. 按用户+日期统计已完成的日志数量 */
    long countByUserIdAndTaskDateAndStatus(Long userId, LocalDate taskDate, TaskStatus status);

}

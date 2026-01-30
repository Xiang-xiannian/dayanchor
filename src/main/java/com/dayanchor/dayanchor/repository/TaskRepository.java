package com.dayanchor.dayanchor.repository;

import com.dayanchor.dayanchor.entity.Task;
import com.dayanchor.dayanchor.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

/** Task DAO. 任务 DAO */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /** Find tasks by userId + status. 按用户+状态查询任务 */
    List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);

    /** Count completed tasks by userId + date. 按用户+日期统计已完成的任务数量 */
    long countByUserIdAndTaskDateAndStatus(Long userId, LocalDate taskDate, TaskStatus status);

}

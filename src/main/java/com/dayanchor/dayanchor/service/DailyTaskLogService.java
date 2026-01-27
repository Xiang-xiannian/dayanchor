package com.dayanchor.dayanchor.service;

import com.dayanchor.dayanchor.entity.DailyTaskLog;
import com.dayanchor.dayanchor.entity.DailyTaskTemplate;
import com.dayanchor.dayanchor.entity.TaskStatus;
import com.dayanchor.dayanchor.repository.DailyTaskLogRepository;
import com.dayanchor.dayanchor.repository.DailyTaskTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DailyTaskLogService {

    private final DailyTaskTemplateRepository templateRepository;
    private final DailyTaskLogRepository logRepository;

    public DailyTaskLogService(
            DailyTaskTemplateRepository templateRepository,
            DailyTaskLogRepository logRepository) {
        this.templateRepository = templateRepository;
        this.logRepository = logRepository;
    }

    public void generateTodayLogs() {
        LocalDate today = LocalDate.now();
        List<DailyTaskTemplate> templates = templateRepository.findAll();

        for (DailyTaskTemplate template : templates) {
            if (!template.isActive()) {
                continue;
            }

            boolean exists = logRepository.existsByTemplateIdAndTaskDate(
                    template.getId(),
                    today);

            if (exists) {
                continue;
            }

            DailyTaskLog log = new DailyTaskLog();
            log.setUserId(template.getUserId());
            log.setTemplate(template);
            log.setTaskDate(today);
            log.setStatus(TaskStatus.PENDING);
            log.setCreatedAt(LocalDateTime.now());

            logRepository.save(log);
        }
    }

    public List<DailyTaskLog> getLogsByDate(Long userId, LocalDate date) {
        return logRepository.findByUserIdAndTaskDate(userId, date);
    }

    @Transactional
    public DailyTaskLog completeLog(Long id) {
        DailyTaskLog log = logRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Daily log not found"));

        log.setStatus(TaskStatus.COMPLETED);

        return logRepository.save(log);
    }
}

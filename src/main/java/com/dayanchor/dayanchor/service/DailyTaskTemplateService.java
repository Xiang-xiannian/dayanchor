package com.dayanchor.dayanchor.service;

import com.dayanchor.dayanchor.dto.UpdateDailyTaskTemplateRequest;
import com.dayanchor.dayanchor.entity.DailyTaskTemplate;
import com.dayanchor.dayanchor.entity.EnergyLevel;
import com.dayanchor.dayanchor.repository.DailyTaskTemplateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/** DailyTaskTemplate service. 每日模板服务 */
@Service
public class DailyTaskTemplateService {
    private final DailyTaskTemplateRepository templateRepository;

    public DailyTaskTemplateService(DailyTaskTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    /** Create template. 创建模板 */
    public DailyTaskTemplate createTemplate(
            Long userId,
            String title,
            String description,
            EnergyLevel energyLevel,
            boolean active) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (energyLevel == null) {
            throw new IllegalArgumentException("Energy level is required");
        }

        DailyTaskTemplate template = new DailyTaskTemplate();
        template.setUserId(userId);
        template.setTitle(title);
        template.setDescription(description);
        template.setEnergyLevel(energyLevel);
        template.setActive(active);
        template.setCreatedAt(LocalDateTime.now());

        return templateRepository.save(template);
    }

    /** List active templates. 查询启用模板 */
    public List<DailyTaskTemplate> getActiveTemplates(Long userId) {
        return templateRepository.findByUserIdAndActiveTrue(userId);
    }

    /** Update template. 更新模板 */
    public DailyTaskTemplate updateTemplate(Long id, UpdateDailyTaskTemplateRequest request) {
        DailyTaskTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (request.getTitle() != null) {
            template.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            template.setDescription(request.getDescription());
        }

        if (request.getEnergyLevel() != null) {
            template.setEnergyLevel(request.getEnergyLevel());
        }

        if (request.getActive() != null) {
            template.setActive(request.getActive());
        }

        return templateRepository.save(template);
    }

    /** Delete template. 删除模板 */
    public void deleteTemplate(Long id) {
        if (!templateRepository.existsById(id)) {
            throw new RuntimeException("Template not found");
        }

        templateRepository.deleteById(id);
    }
}


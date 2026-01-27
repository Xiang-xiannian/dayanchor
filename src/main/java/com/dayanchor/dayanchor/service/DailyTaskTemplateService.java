package com.dayanchor.dayanchor.service;

import com.dayanchor.dayanchor.dto.UpdateDailyTaskTemplateRequest;
import com.dayanchor.dayanchor.entity.DailyTaskTemplate;
import com.dayanchor.dayanchor.entity.EnergyLevel;
import com.dayanchor.dayanchor.repository.DailyTaskTemplateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DailyTaskTemplateService {
    private final DailyTaskTemplateRepository templateRepository;

    public DailyTaskTemplateService(DailyTaskTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

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

    public List<DailyTaskTemplate> getActiveTemplates(Long userId) {
        return templateRepository.findByUserIdAndActiveTrue(userId);
    }

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

    public void deleteTemplate(Long id) {
        if (!templateRepository.existsById(id)) {
            throw new RuntimeException("Template not found");
        }

        templateRepository.deleteById(id);
    }
}

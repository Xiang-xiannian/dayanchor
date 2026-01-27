package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.dto.CreateDailyTaskTemplateRequest;
import com.dayanchor.dayanchor.dto.UpdateDailyTaskTemplateRequest;
import com.dayanchor.dayanchor.entity.DailyTaskTemplate;
import com.dayanchor.dayanchor.service.DailyTaskTemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/daily-templates")
public class DailyTaskTemplateController {

    private final DailyTaskTemplateService templateService;

    public DailyTaskTemplateController(DailyTaskTemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    public DailyTaskTemplate createTemplate(@RequestBody CreateDailyTaskTemplateRequest request) {
        return templateService.createTemplate(
                request.getUserId(),
                request.getTitle(),
                request.getDescription(),
                request.getEnergyLevel(),
                request.isActive());
    }

    @GetMapping
    public List<DailyTaskTemplate> getTemplates(@RequestParam Long userId) {
        return templateService.getActiveTemplates(userId);
    }

    @PatchMapping("/{id}")
    public DailyTaskTemplate updateTemplate(
            @PathVariable Long id,
            @RequestBody UpdateDailyTaskTemplateRequest request) {
        return templateService.updateTemplate(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
    }
}

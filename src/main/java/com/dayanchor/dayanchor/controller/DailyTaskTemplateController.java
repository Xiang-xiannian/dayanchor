package com.dayanchor.dayanchor.controller;

import com.dayanchor.dayanchor.dto.CreateDailyTaskTemplateRequest;
import com.dayanchor.dayanchor.dto.UpdateDailyTaskTemplateRequest;
import com.dayanchor.dayanchor.entity.DailyTaskTemplate;
import com.dayanchor.dayanchor.service.DailyTaskTemplateService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Daily template APIs. 每日模板接口 */
@RestController
@RequestMapping("/api/daily-templates")
public class DailyTaskTemplateController {

    private final DailyTaskTemplateService templateService;

    public DailyTaskTemplateController(DailyTaskTemplateService templateService) {
        this.templateService = templateService;
    }

    /** Create template. 创建模板 */
    @PostMapping
    public DailyTaskTemplate createTemplate(@RequestBody CreateDailyTaskTemplateRequest request) {
        return templateService.createTemplate(
                request.getUserId(),
                request.getTitle(),
                request.getDescription(),
                request.getEnergyLevel(),
                request.isActive());
    }

    /** List active templates. 查询启用模板 */
    @GetMapping
    public List<DailyTaskTemplate> getTemplates(@RequestParam Long userId) {
        return templateService.getActiveTemplates(userId);
    }

    /** Update template. 更新模板 */
    @PatchMapping("/{id}")
    public DailyTaskTemplate updateTemplate(@PathVariable Long id, @RequestBody UpdateDailyTaskTemplateRequest request) {
        return templateService.updateTemplate(id, request);
    }

    /** Delete template. 删除模板 */
    @DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
    }
}


package com.dayanchor.dayanchor.dto;

import com.dayanchor.dayanchor.entity.EnergyLevel;

public class UpdateDailyTaskTemplateRequest {

    private String title;
    private String description;
    private EnergyLevel energyLevel;
    private Boolean active;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EnergyLevel getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(EnergyLevel energyLevel) {
        this.energyLevel = energyLevel;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}

package com.dayanchor.dayanchor.dto;


import com.dayanchor.dayanchor.entity.EnergyLevel;

public class UpdateTaskRequest {
    private String title;

    private EnergyLevel energyLevel;

    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EnergyLevel getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(EnergyLevel energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.test.prediction;

import com.test.model.Resources;
import com.test.service.EcosystemService;
import com.test.util.LoggerUtil;

public class EcosystemSimulation {
    private EcosystemService ecosystemService;
    private int daysToSimulate;
    private int currentDay;

    public EcosystemSimulation(EcosystemService ecosystemService, int daysToSimulate) {
        this.ecosystemService = ecosystemService;
        this.daysToSimulate = daysToSimulate;
        this.currentDay = 0;
    }

    public void initializeSimulation() {
        loadInitialSeasonAndResources();
    }

    private void loadInitialSeasonAndResources() {
        Resources initialResources = ecosystemService.getResourcesFromFile();
        ecosystemService.setResources(initialResources);
        LoggerUtil.logInfo("Initial season and resources loaded: " + initialResources.getSeason() + " " + initialResources);
    }

    public void runSimulation() {
        initializeSimulation();

        for (int day = 0; day < daysToSimulate; day++) {
            currentDay++;
            ecosystemService.changeSeason();
            ecosystemService.consumeWaterByAnimals();
            ecosystemService.updateFoodInteractions();
            ecosystemService.reproduceAnimals();
            ecosystemService.growPlants();
            ecosystemService.showEcosystem();
            LoggerUtil.logInfo("-----Day " + currentDay + " completed.-----");
        }
    }
}

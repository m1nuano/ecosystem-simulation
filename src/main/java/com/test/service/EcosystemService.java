package com.test.service;

import com.test.model.enums.Seasons;
import com.test.model.Animal;
import com.test.model.Plant;
import com.test.model.Resources;
import com.test.util.EcosystemFileManager;
import com.test.util.LoggerUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

import static com.test.util.EcosystemFileManager.parseResources;

@Getter
@Setter
public class EcosystemService {
    private static final String BASE_DIRECTORY = "ecosystems";
    private static final String ecosystemFilePath = "ecosystem.txt";
    private List<Animal> animals = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();
    private Resources resources;
    private final int baseTemperature;
    private int daysPassed = 0;
    private final SecureRandom random = new SecureRandom();


    public EcosystemService(boolean loadFromFile, String ecosystemName) {
        this.animals = new ArrayList<>();
        this.plants = new ArrayList<>();
        this.resources = new Resources();

        if (loadFromFile) {
            EcosystemFileManager.loadEcosystem(this.animals, this.plants, this.resources, ecosystemName);
            this.animals = getAnimals();
            this.plants = getPlants();
            this.resources = getResources();
            this.baseTemperature = resources.getTemperature();
        } else {
            this.resources = new Resources(20, 100, 100, Seasons.Spring);
            this.baseTemperature = resources.getTemperature();
        }
    }


    public void loadEcosystem(String ecosystemName) {
        EcosystemFileManager.loadEcosystem(animals, plants, resources, ecosystemName);
        System.out.println("Ecosystem loaded: " + ecosystemName);
        System.out.println("\nPress Enter to continue");
    }


    public void addAnimal(Animal animal) {
        animals.add(animal);
        LoggerUtil.logInfo("Animal added: " + animal);
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
        LoggerUtil.logInfo("Plant added: " + plant);
    }

    public void removePlant(String plantName) {
        Iterator<Plant> iterator = plants.iterator();
        while (iterator.hasNext()) {
            Plant plant = iterator.next();
            if (plant.getName().equalsIgnoreCase(plantName)) {
                iterator.remove();
                LoggerUtil.logInfo(plantName + " has been removed from the ecosystem.");
            }
        }
    }

    public void removeAnimal(String animalName) {
        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal.getName().equalsIgnoreCase(animalName)) {
                iterator.remove();
                LoggerUtil.logInfo(animalName + " has been removed from the ecosystem.");
            }
        }
    }

    public void setResources(Resources resources) {
        this.resources = resources;
        LoggerUtil.logInfo("Resources updated: " + resources);
    }

    public void showEcosystem() {
        System.out.println("Current Ecosystem:");
        System.out.println("Animals: " + animals);
        System.out.println("Plants: " + plants);
        System.out.println("Resources: " + resources);
        LoggerUtil.logInfo("Ecosystem displayed.");
    }

    public void growPlants() {
        int growthFactor;

        for (Plant plant : plants) {
            if (plant.getPopulation() > 0) {
                int currentPopulation = plant.getPopulation();
                int growthAmount;

                growthFactor = calculateGrowthFactor() + 4;
                growthAmount = Math.max(3, (int) (currentPopulation * random.nextDouble() * 0.005) + growthFactor); // Minimum 3


                if (growthAmount < 1) {
                    growthAmount = 1;
                }

                plant.setPopulation(plant.getPopulation() + growthAmount);
                LoggerUtil.logInfo(plant.getName() + " grew: new population is " + plant.getPopulation());
            }
        }
    }


    private int calculateGrowthFactor() {
        int temperature = resources.getTemperature();
        int humidity = resources.getHumidity();

        int growthFactor = 1;

        if (temperature >= 20 && temperature <= 30 && humidity >= 50 && humidity <= 100) {
            growthFactor = 5;
        } else if (temperature < 20 || temperature > 30 || humidity < 50) {
            growthFactor = 2;
        } else {
            growthFactor = 0;
        }
        return growthFactor;
    }


    public void consumeWaterByAnimals() {
        for (Animal animal : animals) {
            double waterNeeded = animal.getPopulation() * animal.getWaterIntake();
            if (resources.getWater() >= waterNeeded) {
                resources.setWater(resources.getWater() - waterNeeded);
            } else {
                // Decrease in population if water is not enough
                animal.setPopulation(Math.max(0, animal.getPopulation() - 1));
            }
        }
    }

    public Resources getResourcesFromFile() {
        Resources resources = new Resources(20, 100, 100, Seasons.Spring); // Default values
        try (BufferedReader reader = new BufferedReader(new FileReader(ecosystemFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Resources:")) {
                    resources = parseResources(line);
                    break;
                }
            }
        } catch (IOException e) {
            LoggerUtil.logError("Error reading resources from file", e);
        }
        return resources;
    }

    private Map<Animal, Integer> starvationCycles = new HashMap<>();

    public void updateFoodInteractions() {
        for (Animal herbivore : animals) {
            if (herbivore.isHerbivore()) {
                double foodNeeded = herbivore.getFoodIntake() * herbivore.getPopulation();

                for (Plant plant : plants) {
                    if (plant.getPopulation() > 0 && foodNeeded > 0) {
                        if (random.nextDouble() <= 0.65) {
                            int unitsConsumed = (int) Math.min(foodNeeded / plant.getWeight(), plant.getPopulation());
                            plant.setPopulation(plant.getPopulation() - unitsConsumed);
                            foodNeeded -= unitsConsumed * plant.getWeight();
                            LoggerUtil.logInfo(herbivore.getName() + " consumed " + unitsConsumed + " units of " + plant.getName() + "(s).");
                        }
                    }
                }
            }
        }
        applyStarvation();

        for (Animal predator : animals) {
            if (predator.isPredator()) {
                double foodNeeded = predator.getFoodIntake() * predator.getPopulation();
                boolean foundFood = false;

                for (Animal prey : animals) {
                    if (predator.getPopulation() > 0 && prey.getPopulation() > 0 && predator != prey) {
                        boolean canHunt = false;
                        double successChance = 1.0;

                        if (prey.isHerbivore()) {
                            if (predator.getLevel() >= prey.getLevel()) {
                                canHunt = true;
                            } else if (predator.getLevel() == prey.getLevel() - 1) {
                                successChance = 0.42;
                                canHunt = true;
                            } else if (predator.getLevel() == prey.getLevel() - 2) {
                                successChance = 0.35;
                                canHunt = true;
                            } else if (predator.getLevel() == prey.getLevel() - 3) {
                                successChance = 0.27;
                                canHunt = true;
                            } else if (predator.getLevel() == prey.getLevel() - 4) {
                                successChance = 0.18;
                                canHunt = true;
                            }
                        } else if (prey.isPredator()) {
                            if (predator.getLevel() < prey.getLevel()) {
                                canHunt = true;
                            } else if (predator.getLevel() == 5 && prey.getLevel() == 5) {
                                successChance = 0.09;
                                canHunt = true;
                            } else if (predator.getLevel() > prey.getLevel()) {
                                canHunt = true;
                            }
                        }

                        if (canHunt && random.nextDouble() <= successChance) {
                            int unitsConsumed = (int) Math.min(prey.getPopulation(), foodNeeded / prey.getWeight());
                            prey.setPopulation(prey.getPopulation() - unitsConsumed);
                            foodNeeded -= unitsConsumed * prey.getWeight();
                            foundFood = unitsConsumed > 0;

                            if (unitsConsumed > 0) {
                                LoggerUtil.logInfo(predator.getName() + " (Level " + predator.getLevel() + ") consumed " +
                                        unitsConsumed + " " + prey.getName() + "(s) (Level " + prey.getLevel() + ").");
                            }
                        }
                        if (foodNeeded <= 0) break;
                    }
                }

                if (!foundFood) {
                    starvationCycles.put(predator, starvationCycles.getOrDefault(predator, 0) + 1);
                } else {
                    starvationCycles.put(predator, 0);
                }
            }
        }
        applyStarvation();
    }

    private void applyStarvation() {
        for (Animal animal : animals) {
            if (animal.getPopulation() > 0) {
                double foodNeeded = animal.getFoodIntake() * animal.getPopulation();
                int hungerCycles = starvationCycles.getOrDefault(animal, 0);

                LoggerUtil.logInfo("Applying starvation check for " + animal.getName() +
                        ". Food needed: " + foodNeeded +
                        ", current population: " + animal.getPopulation());

                if (foodNeeded > animal.getFoodIntake()) {
                    starvationCycles.put(animal, hungerCycles + 1);

                    if (hungerCycles >= 7) {
                        animal.setPopulation(0);
                        LoggerUtil.logInfo(animal.getName() + " has starved and its population is now 0.");
                    } else {
                        LoggerUtil.logInfo(animal.getName() + " is starving but not enough cycles yet.");
                    }
                } else {
                    LoggerUtil.logInfo(animal.getName() + " has enough food. Resetting starvation cycles.");
                    starvationCycles.put(animal, 0);
                }
            }
        }
    }

    public void reproduceAnimals() {
        for (Animal animal : animals) {
            double reproductionChance = animal.isPredator() ? 0.21 : 0.63;

            if (animal.getPopulation() > 2 && checkPreyAvailability(animal) && random.nextDouble() <= reproductionChance) {
                int newAnimals = (int) (animal.getPopulation() * 0.01 + random.nextInt(1));
                animal.setPopulation(animal.getPopulation() + newAnimals);
                LoggerUtil.logInfo(animal.getName() + " reproduced: new population is " + animal.getPopulation());
            }
        }

    }

    private boolean checkPreyAvailability(Animal animal) {
        if (animal.isHerbivore()) {
            int totalPlantPopulation = plants.stream().mapToInt(Plant::getPopulation).sum();
            return totalPlantPopulation >= animal.getPopulation();
        } else if (animal.isPredator()) {
            int totalHerbivorePopulation = animals.stream()
                    .filter(Animal::isHerbivore)
                    .mapToInt(Animal::getPopulation)
                    .sum();
            return totalHerbivorePopulation >= animal.getPopulation();
        }
        return true;
    }

    public void changeSeason() {
        if (++daysPassed >= 91) { //365.25 / 4 = 91
            daysPassed = 0;
            updateSeason();
        }

        applyDailyTemperatureChange();
        generateRandomWeatherEvent();

        LoggerUtil.logInfo("Daily temperature adjusted to: " + resources.getTemperature());
        LoggerUtil.logInfo("Humidity level is: " + resources.getHumidity());
    }

    private void updateSeason() {
        switch (resources.getSeason()) {
            case Winter -> resources.setSeason(Seasons.Spring);
            case Spring -> resources.setSeason(Seasons.Summer);
            case Summer -> resources.setSeason(Seasons.Autumn);
            case Autumn -> resources.setSeason(Seasons.Winter);
        }
        LoggerUtil.logInfo("Season changed to: " + resources.getSeason());
    }


    private void applyDailyTemperatureChange() {
        int dailyTemperatureChange = random.nextInt(-5, 6);  // Daily change within -5 to +5

        int minTemperature = calculateMinTemperature();
        int maxTemperature = calculateMaxTemperature();

        int newTemperature = resources.getTemperature() + dailyTemperatureChange;

        if (newTemperature < minTemperature) {
            newTemperature = minTemperature;
        } else if (newTemperature > maxTemperature) {
            newTemperature = maxTemperature;
        }
        resources.setTemperature(newTemperature);
    }

    private int calculateMinTemperature() {
        switch (resources.getSeason()) {
            case Winter -> {
                return baseTemperature - 30;
            }
            case Spring, Autumn -> {
                return baseTemperature - 15;
            }
            case Summer -> {
                return baseTemperature - 5;
            }
            default -> throw new IllegalStateException("Unexpected season: " + resources.getSeason());
        }
    }

    private int calculateMaxTemperature() {
        switch (resources.getSeason()) {
            case Winter -> {
                return baseTemperature - 10;
            }
            case Spring, Autumn -> {
                return baseTemperature;
            }
            case Summer -> {
                return baseTemperature + 15;
            }
            default -> throw new IllegalStateException("Unexpected season: " + resources.getSeason());
        }
    }

    private void generateRandomWeatherEvent() {
        int eventChance = random.nextInt(10) + 1;
        int waterAdjustment;

        if (eventChance <= 6) {
            int maxRainAmount = (int) (resources.getWater() * 0.05);
            waterAdjustment = random.nextInt(maxRainAmount) + 1;
            resources.setWater(resources.getWater() + waterAdjustment);
            resources.setTemperature(resources.getTemperature() - random.nextInt(1, 2));
            resources.setHumidity(Math.min(100, resources.getHumidity() + (random.nextInt(4) + 1)));
            LoggerUtil.logInfo("Weather Event: Rain occurred, + " + waterAdjustment + " water resources. Humidity increased.");
        } else if (eventChance >= 8) {
            int maxDroughtAmount = (int) (resources.getWater() * 0.05);
            waterAdjustment = random.nextInt(maxDroughtAmount) + 1;
            resources.setWater(Math.max(0, resources.getWater() - waterAdjustment));
            resources.setTemperature(resources.getTemperature() + random.nextInt(1, 2));
            resources.setHumidity(Math.max(0, resources.getHumidity() - (random.nextInt(4) + 1)));
            LoggerUtil.logInfo("Weather Event: Drought occurred, - " + waterAdjustment + " water resources. Humidity decreased.");
        } else {
            LoggerUtil.logInfo("Weather Event: No significant weather event today.");
        }
    }


    public Map<String, String> predictPopulationChanges(int days) {
        LoggerUtil.logInfo("Start calculating population forecasts for" + days + "days");
        Map<String, String> predictions = new HashMap<>();

        Map<String, Integer> initialPopulations = new HashMap<>();
        for (Animal animal : animals) {
            initialPopulations.put(animal.getName(), animal.getPopulation());
        }

        // Simulate changes for a specified period
        for (int day = 0; day < days; day++) {
            consumeWaterByAnimals();
            reproduceAnimals();
            growPlants();
            updateFoodInteractions();
            changeSeason();
        }
        // Comparing populations after simulation
        for (Animal animal : animals) {
            int initialPopulation = initialPopulations.get(animal.getName());
            int currentPopulation = animal.getPopulation();

            String prediction;
            if (currentPopulation > initialPopulation) {
                prediction = "will Increase";
            } else if (currentPopulation < initialPopulation) {
                prediction = "will Decrease";
            } else {
                prediction = "stable";
            }

            predictions.put(animal.getName(), prediction);
            LoggerUtil.logInfo("For the animal" + animal.getName() + " forecast " + prediction);
        }

        LoggerUtil.logInfo("The forecast calculation is complete.");
        return predictions;
    }
}

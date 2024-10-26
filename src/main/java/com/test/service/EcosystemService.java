package com.test.service;

import com.test.model.Animal;
import com.test.model.Plant;
import com.test.model.Resources;
import com.test.util.LoggerUtil;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EcosystemService {
    private static final String ecosystemFilePath = "ecosystem.txt";
    private List<Animal> animals = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();
    private Resources resources = new Resources(25, 50, 100);

    public void saveEcosystem() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ecosystemFilePath))) {
            writer.write("# Animals\n");
            for (Animal animal : animals) {
                writer.write(String.format("Animal: name=%s, type=%s, population=%d\n",
                        animal.getName(), animal.getType(), animal.getPopulation()));
            }
            writer.write("\n# Plants\n");
            for (Plant plant : plants) {
                writer.write(String.format("Plant: name=%s, height=%d, population=%d\n",
                        plant.getName(), plant.getHeight(), plant.getPopulation()));
            }
            writer.write("\n# Resources\n");
            writer.write(String.format("Resources: temperature=%d, humidity=%d, water=%d\n",
                    resources.getTemperature(), resources.getHumidity(), resources.getWater()));
            LoggerUtil.logInfo("Ecosystem saved successfully.");
        } catch (IOException e) {
            LoggerUtil.logError("Error saving ecosystem to text file", e);
        }
    }

    public void loadEcosystem() {
        animals.clear();
        plants.clear();
        resources = new Resources(25, 50, 100);

        try (BufferedReader reader = new BufferedReader(new FileReader(ecosystemFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Animal:")) {
                    Animal animal = parseAnimal(line);
                    animals.add(animal);
                } else if (line.startsWith("Plant:")) {
                    Plant plant = parsePlant(line);
                    plants.add(plant);
                } else if (line.startsWith("Resources:")) {
                    resources = parseResources(line);
                }
            }
            LoggerUtil.logInfo("Ecosystem loaded successfully.");
        } catch (IOException e) {
            LoggerUtil.logInfo("No existing data found or error loading file: initializing new ecosystem.");
        }
    }

    private Animal parseAnimal(String line) {
        Map<String, String> values = parseLine(line, "Animal:");
        return new Animal(values.get("name"), values.get("type"), Integer.parseInt(values.get("population")));
    }

    private Plant parsePlant(String line) {
        Map<String, String> values = parseLine(line, "Plant:");
        return new Plant(values.get("name"), Integer.parseInt(values.get("height")), Integer.parseInt(values.get("population")));
    }

    private Resources parseResources(String line) {
        Map<String, String> values = parseLine(line, "Resources:");
        return new Resources(Integer.parseInt(values.get("temperature")),
                Integer.parseInt(values.get("humidity")),
                Integer.parseInt(values.get("water")));
    }

    private Map<String, String> parseLine(String line, String prefix) {
        line = line.replace(prefix, "").trim();
        String[] parts = line.split(", ");
        Map<String, String> values = new HashMap<>();
        for (String part : parts) {
            String[] keyValue = part.split("=");
            values.put(keyValue[0].trim(), keyValue[1].trim());
        }
        return values;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        LoggerUtil.logInfo("Animal added: " + animal);
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
        LoggerUtil.logInfo("Plant added: " + plant);
    }

    public void removeAnimal(String name) {
        if (animals.removeIf(animal -> animal.getName().equalsIgnoreCase(name))) {
            LoggerUtil.logInfo("Animal removed: " + name);
        } else {
            LoggerUtil.logInfo("Animal not found for removal: " + name);
        }
    }

    public void removePlant(String name) {
        if (plants.removeIf(plant -> plant.getName().equalsIgnoreCase(name))) {
            LoggerUtil.logInfo("Plant removed: " + name);
        } else {
            LoggerUtil.logInfo("Plant not found for removal: " + name);
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
}

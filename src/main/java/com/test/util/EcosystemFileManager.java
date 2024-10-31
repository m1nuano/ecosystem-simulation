package com.test.util;

import com.test.model.Animal;
import com.test.model.Plant;
import com.test.model.Resources;
import com.test.model.enums.AnimalType;
import com.test.model.enums.Seasons;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class EcosystemFileManager {

    private static final String ecosystemBaseDirectory = "ecosystems";

    public static void saveEcosystem(List<Animal> animals, List<Plant> plants, Resources resources) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a name to save the ecosystem: ");
        String saveName = scanner.nextLine();

        Path saveDirectory = Paths.get(ecosystemBaseDirectory, saveName);
        Path ecosystemFilePath = saveDirectory.resolve(saveName + ".txt");
        Path logFilePath = saveDirectory.resolve(saveName + "_log.txt");

        try {
            Files.createDirectories(saveDirectory);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ecosystemFilePath.toFile()))) {
                writer.write("# Animals\n");
                for (Animal animal : animals) {
                    writer.write(String.format(
                            "Animal: name=%s, type=%s, population=%d, weight=%.2f, foodIntake=%.2f, waterIntake=%.2f, level=%d\n",
                            animal.getName(),
                            animal.getType().name(),
                            animal.getPopulation(),
                            animal.getWeight(),
                            animal.getFoodIntake(),
                            animal.getWaterIntake(),
                            animal.getLevel()));
                }

                writer.write("\n# Plants\n");
                for (Plant plant : plants) {
                    writer.write(String.format("Plant: name=%s, weight=%d, population=%d, waterConsumption=%.2f\n",
                            plant.getName(),
                            plant.getWeight(),
                            plant.getPopulation(),
                            plant.getWaterConsumption()));
                }

                writer.write("\n# Resources\n");
                writer.write(String.format("Resources: temperature=%d, humidity=%d, water=%.2f, season=%s\n",
                        resources.getTemperature(),
                        resources.getHumidity(),
                        resources.getWater(),
                        resources.getSeason()));
            }

            boolean createLog = false;
            while (true) {
                System.out.print("Would you like to create log file? (yes/no): ");
                String choice = scanner.nextLine().trim().toLowerCase();

                if (choice.equals("yes")) {
                    createLog = true;
                    break;
                } else if (choice.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }

            if (createLog) {

                LoggerUtil.initializeLogFile(saveName);
                System.out.println("Temporary logfile created successfully.");

                System.out.println("Logfile options:");
                System.out.println("1. Append to existing ecosystem log file.");
                System.out.println("2. Discard the log buffer (keep only in temp_log.txt).");
                System.out.println("3. Save as a new log file for this save.");
                System.out.print("Choose an option (default is to append): ");

                String logInput = scanner.nextLine().trim();
                int logOption = logInput.isEmpty() ? 1 : Integer.parseInt(logInput);


                switch (logOption) {
                    case 1:
                        LoggerUtil.flushLogToFile(logFilePath, true);
                        break;
                    case 2:
                        System.out.println("Log buffer retained only in memory.");
                        break;
                    case 3:
                        LoggerUtil.flushLogToFile(logFilePath, false);
                        break;
                    default:
                        System.out.println("Invalid option. Log file unchanged.");
                        break;
                }
            } else {
                System.out.println("Ecosystem saved without log.");
            }

        } catch (IOException e) {
            LoggerUtil.logError("Error saving ecosystem", e);
        }
    }

    public static void loadEcosystem(List<Animal> animals, List<Plant> plants, Resources resources, String ecosystemName) {
        animals.clear();
        plants.clear();

        Path ecosystemFilePath = Paths.get(ecosystemBaseDirectory, ecosystemName, ecosystemName + ".txt");

        LoggerUtil.initializeLogFile(ecosystemName);

        try (BufferedReader reader = new BufferedReader(new FileReader(ecosystemFilePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Animal:")) {
                    Animal animal = parseAnimal(line);
                    animals.add(animal);
                } else if (line.startsWith("Plant:")) {
                    Plant plant = parsePlant(line);
                    plants.add(plant);
                } else if (line.startsWith("Resources:")) {
                    Resources loadedResources = parseResources(line);
                    resources.setTemperature(loadedResources.getTemperature());
                    resources.setHumidity(loadedResources.getHumidity());
                    resources.setWater(loadedResources.getWater());
                    resources.setSeason(loadedResources.getSeason());
                }
            }
            LoggerUtil.logInfo("The ecosystem has loaded successfully.");
        } catch (IOException e) {
            LoggerUtil.logError("Error loading ecosystem: " + ecosystemName, e);
            resources.setTemperature(20);
            resources.setHumidity(100);
            resources.setWater(100);
            resources.setSeason(Seasons.Spring);
        }
    }

    private static Animal parseAnimal(String line) {
        Map<String, String> values = parseLine(line, "Animal:");
        return new Animal(
                values.get("name"),
                AnimalType.valueOf(values.get("type")),
                Integer.parseInt(values.get("population")),
                Double.parseDouble(values.get("weight").replace(",", ".")),
                Double.parseDouble(values.get("foodIntake").replace(",", ".")),
                Double.parseDouble(values.get("waterIntake").replace(",", ".")),
                Integer.parseInt(values.get("level"))
        );
    }

    private static Plant parsePlant(String line) {
        Map<String, String> values = parseLine(line, "Plant:");
        return new Plant(
                values.get("name"),
                Integer.parseInt(values.get("weight")),
                Integer.parseInt(values.get("population")),
                Double.parseDouble(values.get("waterConsumption").replace(",", "."))
        );
    }

    public static Resources parseResources(String line) {
        Map<String, String> values = parseLine(line, "Resources:");
        return new Resources(
                Integer.parseInt(values.get("temperature")),
                Integer.parseInt(values.get("humidity")),
                Double.parseDouble(values.get("water").replace(",", ".")),
                Seasons.valueOf(values.get("season"))
        );
    }

    private static Map<String, String> parseLine(String line, String prefix) {
        line = line.replace(prefix, "").trim();
        String[] parts = line.split(", ");
        Map<String, String> values = new HashMap<>();
        for (String part : parts) {
            String[] keyValue = part.split("=");
            values.put(keyValue[0].trim(), keyValue[1].trim());
        }
        return values;
    }
}

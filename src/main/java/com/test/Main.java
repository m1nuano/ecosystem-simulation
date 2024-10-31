package com.test;

import com.test.model.enums.AnimalType;
import com.test.model.enums.Seasons;
import com.test.model.Animal;
import com.test.model.Plant;
import com.test.model.Resources;
import com.test.prediction.EcosystemSimulation;
import com.test.service.EcosystemService;
import com.test.util.EcosystemFileManager;
import com.test.util.LoggerUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EcosystemService ecosystemService = null;

        if (yourChoice(scanner)) {
            String selectedEcosystem = selectEcosystem(scanner);
            ecosystemService = new EcosystemService(true, selectedEcosystem);

            if (!selectedEcosystem.isEmpty()) {
                ecosystemService.loadEcosystem(selectedEcosystem);
            } else {
                System.out.println("Выбрана пустая экосистема или ошибка загрузки, инициализация с новыми значениями.");
            }
        } else {
            ecosystemService = new EcosystemService(false, null);
        }

        scanner.nextLine();
        while (true) {
            System.out.println("\nEcosystem Simulator - Options:");
            System.out.println("1. Add Animal");
            System.out.println("2. Add Plant");
            System.out.println("3. Set Resources");
            System.out.println("4. Show Ecosystem");
            System.out.println("5. Remove Animal");
            System.out.println("6. Remove Plant");
            System.out.println("7. Run Simulation");
            System.out.println("8. Get Predictions");
            System.out.println("9. Save Current Ecosystem");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1: // Add animal
                    addAnimal(scanner, ecosystemService);
                    break;

                case 2: // Add plant
                    addPlant(scanner, ecosystemService);
                    break;

                case 3: // Set resources
                    setResources(scanner, ecosystemService);
                    break;

                case 4: // Show ecosystem
                    ecosystemService.showEcosystem();
                    break;

                case 5: // Remove animal
                    removeAnimal(scanner, ecosystemService);
                    break;

                case 6: // Remove plant
                    removePlant(scanner, ecosystemService);
                    break;

                case 7: // Simulations
                    runSimulation(scanner, ecosystemService);
                    break;

                case 8: // Predictions
                    getPredictions(ecosystemService);
                    break;

                case 9: // Save
                    saveEcosystemWithLogOptions(ecosystemService);
                    break;

                case 0:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static boolean yourChoice(Scanner scanner) {
        int choice = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Press 1 to create an empty Ecosystem with starting resources (they can be edited)");
            System.out.println("Press 2 to choose an existing ecosystem (in the ecosystems/** folder)");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                validInput = (choice == 1 || choice == 2);
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return choice == 2;
    }

    private static String selectEcosystem(Scanner scanner) {
        System.out.println("Available Ecosystems:");
        try {
            Files.list(Path.of("ecosystems"))
                    .filter(Files::isDirectory)
                    .forEach(path -> System.out.println("- " + path.getFileName()));
        } catch (Exception e) {
            System.out.println("Error listing ecosystems: " + e.getMessage());
        }

        System.out.print("Enter the name of the ecosystem you want to load: ");
        scanner.nextLine();
        return scanner.nextLine().trim();
    }

    private static void addAnimal(Scanner scanner, EcosystemService ecosystemService) {
        System.out.print("Enter Animal Name: ");
        String a_name = scanner.nextLine();

        System.out.println("Enter Animal Type (0 for PREDATOR, 1 for HERBIVORE): ");
        for (AnimalType type : AnimalType.values()) {
            System.out.printf("%d - %s%n", type.ordinal(), type);
        }
        int typeIndex = scanner.nextInt();
        AnimalType a_type;
        if (typeIndex >= 0 && typeIndex < AnimalType.values().length) {
            a_type = AnimalType.values()[typeIndex];
        } else {
            System.out.println("Invalid type selected, defaulting to HERBIVORE.");
            a_type = AnimalType.Herbivore;
        }

        System.out.print("Enter Animal Population: ");
        int a_population = scanner.nextInt();
        System.out.print("Enter Animal Weight: ");
        int a_weight = scanner.nextInt();
        System.out.print("Enter Animal Food Intake: ");
        double a_food_intake = scanner.nextDouble();
        System.out.print("Enter Animal Water Intake: ");
        double a_water_intake = scanner.nextDouble();
        System.out.println("Enter Animal Level Hierarchy: ");
        int a_level = scanner.nextInt();

        ecosystemService.addAnimal(new Animal(a_name, a_type, a_population, a_weight, a_food_intake, a_water_intake, a_level));
        System.out.println("Animal added: " + a_name);
    }

    private static void addPlant(Scanner scanner, EcosystemService ecosystemService) {
        System.out.print("Enter Plant Name: ");
        String p_name = scanner.nextLine();
        System.out.print("Enter Plant Weight: ");
        int p_weight = scanner.nextInt();
        System.out.print("Enter Plant Population: ");
        int p_population = scanner.nextInt();
        System.out.print("Enter Plant Water Consumption: ");
        double p_water_needed = scanner.nextDouble();

        ecosystemService.addPlant(new Plant(p_name, p_weight, p_population, p_water_needed));
        System.out.println("Plant added: " + p_name);
    }

    private static void setResources(Scanner scanner, EcosystemService ecosystemService) {
        System.out.print("Enter Temperature: ");
        int temperature = scanner.nextInt();
        System.out.print("Enter Humidity: ");
        int humidity = scanner.nextInt();
        System.out.print("Enter Water: ");
        double water = scanner.nextDouble();

        System.out.println("Enter Season (0 for SUMMER, 1 for AUTUMN, 2 for WINTER, 3 for SPRING): ");
        for (Seasons season : Seasons.values()) {
            System.out.printf("%d - %s%n", season.ordinal(), season);
        }
        int seasonIndex = scanner.nextInt();

        Seasons season;
        if (seasonIndex >= 0 && seasonIndex < Seasons.values().length) {
            season = Seasons.values()[seasonIndex];
        } else {
            System.out.println("Invalid season selected, defaulting to SPRING.");
            season = Seasons.Spring;
        }

        ecosystemService.setResources(new Resources(temperature, humidity, water, season));
        System.out.println("Resources and season updated.");
    }

    private static void removeAnimal(Scanner scanner, EcosystemService ecosystemService) {
        System.out.print("Enter Animal Name to Remove: ");
        String nameToRemoveAnimal = scanner.nextLine();
        ecosystemService.removeAnimal(nameToRemoveAnimal);
    }

    private static void removePlant(Scanner scanner, EcosystemService ecosystemService) {
        System.out.print("Enter Plant Name to Remove: ");
        String nameToRemovePlant = scanner.nextLine();
        ecosystemService.removePlant(nameToRemovePlant);
    }

    private static void runSimulation(Scanner scanner, EcosystemService ecosystemService) {
        System.out.print("Enter number of days to simulate: ");
        int daysToSimulate = scanner.nextInt();
        EcosystemSimulation simulation = new EcosystemSimulation(ecosystemService, daysToSimulate);
        simulation.runSimulation();
    }

    private static void getPredictions(EcosystemService ecosystemService) {
        // Predict what will happen in 5 days
        Map<String, String> predictions = ecosystemService.predictPopulationChanges(5);

        System.out.println("Population predictions:");
        for (Map.Entry<String, String> entry : predictions.entrySet()) {
            System.out.println("Population " + entry.getKey() + " will be: " + entry.getValue());
            LoggerUtil.logInfo("Population " + entry.getKey() + " will be: " + entry.getValue());
        }
    }

    private static void saveEcosystemWithLogOptions(EcosystemService ecosystemService) {
        EcosystemFileManager.saveEcosystem(
                ecosystemService.getAnimals(),
                ecosystemService.getPlants(),
                ecosystemService.getResources()
        );

        System.out.println("Ecosystem saved successfully.");
    }
}

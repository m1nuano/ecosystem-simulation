package com.test;

import com.test.model.Animal;
import com.test.model.Plant;
import com.test.model.Resources;
import com.test.service.EcosystemService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EcosystemService ecosystemService = new EcosystemService();
        ecosystemService.loadEcosystem(); // Loading the existing ecosystem, if there is one

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Ecosystem Simulator - Options:");
            System.out.println("1. Add Animal");
            System.out.println("2. Add Plant");
            System.out.println("3. Set Resources");
            System.out.println("4. Show Ecosystem");
            System.out.println("5. Remove Animal");
            System.out.println("6. Remove Plant");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); //

            switch (choice) {
                case 1: // add animal
                    System.out.print("Enter Animal Name: ");
                    String animalName = scanner.nextLine();
                    System.out.print("Enter Animal Type: ");
                    String animalType = scanner.nextLine();
                    System.out.print("Enter Animal Population: ");
                    int animalPopulation = scanner.nextInt();
                    ecosystemService.addAnimal(new Animal(animalName, animalType, animalPopulation));
                    break;

                case 2: // add plant
                    System.out.print("Enter Plant Name: ");
                    String plantName = scanner.nextLine();
                    System.out.print("Enter Plant Height: ");
                    int plantHeight = scanner.nextInt();
                    System.out.print("Enter Plant Population: ");
                    int plantPopulation = scanner.nextInt();
                    ecosystemService.addPlant(new Plant(plantName, plantHeight, plantPopulation));
                    break;

                case 3: // set resources
                    System.out.print("Enter Temperature: ");
                    int temperature = scanner.nextInt();
                    System.out.print("Enter Humidity: ");
                    int humidity = scanner.nextInt();
                    System.out.print("Enter Water: ");
                    int water = scanner.nextInt();
                    ecosystemService.setResources(new Resources(temperature, humidity, water));
                    break;

                case 4: // show ecosystem
                    ecosystemService.showEcosystem();
                    break;

                case 5: // delete animal
                    System.out.print("Enter Animal Name to Remove: ");
                    String nameToRemoveAnimal = scanner.nextLine();
                    ecosystemService.removeAnimal(nameToRemoveAnimal);
                    break;

                case 6: // delete plant
                    System.out.print("Enter Plant Name to Remove: ");
                    String nameToRemovePlant = scanner.nextLine();
                    ecosystemService.removePlant(nameToRemovePlant);
                    break;

                case 0: // exit
                    ecosystemService.saveEcosystem();
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

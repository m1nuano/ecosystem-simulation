package com.test.model;

import com.test.model.enums.AnimalType;
import lombok.*;

@Data
@AllArgsConstructor
public class Animal {
    private String name;
    private AnimalType type;
    private int population;
    private double weight;
    private double foodIntake;
    private double waterIntake;
    private int level;

    public boolean isPredator() {
        return type == AnimalType.Predator;
    }

    public boolean isHerbivore() {
        return type == AnimalType.Herbivore;
    }


}

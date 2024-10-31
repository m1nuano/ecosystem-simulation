package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Plant{
    private String name;
    private int weight;
    private int population;
    private double waterConsumption;
}
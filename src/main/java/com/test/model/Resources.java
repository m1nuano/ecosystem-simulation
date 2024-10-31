package com.test.model;

import com.test.model.enums.Seasons;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resources {
    private int temperature;
    private int humidity;
    private double water;
    private Seasons season;
}

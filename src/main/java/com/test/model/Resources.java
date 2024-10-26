package com.test.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class Resources implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int temperature;
    private int humidity;
    private int water;
}

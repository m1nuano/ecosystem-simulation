package com.test.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class Animal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private int population;
}

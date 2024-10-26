package com.test.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class Plant implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private int height;
    private int population;
}

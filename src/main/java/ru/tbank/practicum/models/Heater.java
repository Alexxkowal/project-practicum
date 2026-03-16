package ru.tbank.practicum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Heater {
    private Long id;
    private double currentTemperature;
    private boolean isWorking;
    private double targetTemperature;
}

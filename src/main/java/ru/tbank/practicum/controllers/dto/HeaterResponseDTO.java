package ru.tbank.practicum.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeaterResponseDTO {
    private Long id;
    private double currentTemperature;
    private double targetTemperature;
    private boolean isWorking;
}

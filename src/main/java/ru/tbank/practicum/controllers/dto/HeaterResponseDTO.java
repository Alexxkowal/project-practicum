package ru.tbank.practicum.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeaterResponseDTO {
    private Long id;
    private double currentTemp;
    private double targetTemp;
    private boolean isWorking;
}

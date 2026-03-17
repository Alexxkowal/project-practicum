package ru.tbank.practicum.controllers.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HeaterRequestDTO {
    private Double targetTemperature;
    private Boolean isWorking;
}

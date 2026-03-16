package ru.tbank.practicum.controllers.dto;

import lombok.Data;

@Data
public class HeaterRequestDTO {
    private Double targetTemperature;
    private Boolean isWorking;
}

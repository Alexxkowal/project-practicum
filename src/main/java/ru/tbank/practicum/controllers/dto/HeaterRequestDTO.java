package ru.tbank.practicum.controllers.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class HeaterRequestDTO {
    @Min(5)
    @Max(35)
    private Double targetTemp;

    private Boolean isWorking;
}

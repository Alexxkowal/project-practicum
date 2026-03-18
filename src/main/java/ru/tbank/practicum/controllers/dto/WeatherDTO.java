package ru.tbank.practicum.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherDTO {
    private Long id;
    private Double temperature;
    private String description;
}

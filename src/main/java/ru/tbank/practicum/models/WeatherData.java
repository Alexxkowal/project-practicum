package ru.tbank.practicum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {
    private Long id;
    private Double temperature;
    private String description;
    private Double lat;
    private Double lon;
    private Integer pressure;
    private Double windSpeed;
    private LocalDateTime time;
}

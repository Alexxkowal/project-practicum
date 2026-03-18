package ru.tbank.practicum.services.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {

    private Coord coord;
    private List<Weather> weather;
    private String base;
    private CommonData main;
    private Integer visibility;
    private Wind wind;
    private Clouds clouds;
    private Long dt;
    private LocationData sys;
    private Integer timezone;
    private Long id;
    private String name;
    private Integer cod;
}

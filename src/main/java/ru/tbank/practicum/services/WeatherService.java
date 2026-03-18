package ru.tbank.practicum.services;

import java.util.List;
import ru.tbank.practicum.controllers.dto.WeatherDTO;

public interface WeatherService {
    WeatherDTO getWeatherByCoordinateAndDate(Double lon, Double lat);

    List<WeatherDTO> findAllWeather();
}

package ru.tbank.practicum.services;

import ru.tbank.practicum.controllers.dto.WeatherDTO;

import java.util.List;

public interface WeatherService {
    WeatherDTO getWeatherByCoordinateAndDate(Double lon, Double lat);

    List<WeatherDTO> findAllWeather();
}

package ru.tbank.practicum.repositories;

import ru.tbank.practicum.models.WeatherData;

import java.time.LocalDate;
import java.util.List;

public interface WeatherRepository {
    List<WeatherData> findAll();

    WeatherData save(WeatherData weatherData);

    WeatherData findById(Long id);

    List<WeatherData> findByDate(LocalDate date);

    List<WeatherData> findByCoordsAndDate(Double lat, Double lon, LocalDate date);
}

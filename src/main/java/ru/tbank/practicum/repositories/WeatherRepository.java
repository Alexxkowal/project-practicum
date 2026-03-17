package ru.tbank.practicum.repositories;

import java.time.LocalDate;
import java.util.List;
import ru.tbank.practicum.models.WeatherData;

public interface WeatherRepository {
    List<WeatherData> findAll();

    WeatherData save(WeatherData weatherData);

    WeatherData findById(Long id);

    List<WeatherData> findByDate(LocalDate date);

    List<WeatherData> findByCoordsAndDate(Double lat, Double lon, LocalDate date);
}

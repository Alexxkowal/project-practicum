package ru.tbank.practicum.mappers;

import org.springframework.stereotype.Component;
import ru.tbank.practicum.controllers.dto.WeatherDTO;
import ru.tbank.practicum.models.WeatherData;
import ru.tbank.practicum.services.dto.WeatherResponse;

import java.time.LocalDateTime;

@Component
public class WeatherMapper {
    public WeatherData mapToModel(WeatherResponse weatherResponse) {
        String description = "No description available"; // Значение по умолчанию
        if (weatherResponse.getWeather() != null && !weatherResponse.getWeather().isEmpty()) {
            description = weatherResponse.getWeather().get(0).getDescription();
        }
        return new WeatherData(
                null,
                weatherResponse.getMain().getTemp(),
                description,
                weatherResponse.getCoord().getLat(),
                weatherResponse.getCoord().getLon(),
                weatherResponse.getMain().getPressure(),
                weatherResponse.getWind().getSpeed(),
                LocalDateTime.now()
        );
    }

    public WeatherDTO mapToDto(WeatherData weatherData) {
        return new WeatherDTO(
                weatherData.getId(),
                weatherData.getTemperature(),
                weatherData.getDescription()
        );
    }


}

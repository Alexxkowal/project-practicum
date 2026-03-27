package ru.tbank.practicum.mappers;

import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tbank.practicum.controllers.dto.WeatherDTO;
import ru.tbank.practicum.models.WeatherData;
import ru.tbank.practicum.services.dto.Weather;
import ru.tbank.practicum.services.dto.WeatherResponse;

@Mapper(
        componentModel = "spring",
        imports = {LocalDateTime.class})
public interface WeatherMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "temperature", source = "main.temp")
    @Mapping(target = "lat", source = "coord.lat")
    @Mapping(target = "lon", source = "coord.lon")
    @Mapping(target = "pressure", source = "main.pressure")
    @Mapping(target = "clouds", source = "clouds.all")
    @Mapping(target = "windSpeed", source = "wind.speed")
    @Mapping(target = "time", expression = "java(LocalDateTime.now())")
    @Mapping(target = "description", source = "weather")
    WeatherData mapToModel(WeatherResponse weatherResponse);

    WeatherDTO mapToDto(WeatherData weatherData);

    default String mapDescription(List<Weather> weather) {
        if (weather == null || weather.isEmpty()) {
            return "No description available";
        }
        return weather.get(0).getDescription();
    }
}

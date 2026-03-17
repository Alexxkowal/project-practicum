package ru.tbank.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tbank.practicum.models.WeatherData;
import ru.tbank.practicum.services.dto.WeatherResponse;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface WeatherMapperTest {
    @Mapping(target = "temperature", source = "main.temp")
    @Mapping(target = "latitude", source = "coord.lat")
    @Mapping(target = "longitude", source = "coord.lon")
    @Mapping(target = "pressure", source = "main.pressure")
    @Mapping(target = "windSpeed", source = "wind.speed")
    @Mapping(target = "timestamp", expression = "java(LocalDateTime.now())")
    @Mapping(target = "description", source = "weather")
    WeatherData mapToModel(WeatherResponse weatherResponse);

    default String mapDescription(List<WeatherDescription> weather) {
        if (weather == null || weather.isEmpty()) {
            return "No description available";
        }
        return weather.get(0).getDescription();
    }

}

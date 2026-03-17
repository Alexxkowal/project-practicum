package ru.tbank.practicum.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.mappers.WeatherMapper;
import ru.tbank.practicum.services.WeatherService;

@Component
@Slf4j
public class WeatherScheduler {
    private final WeatherService weatherService;
    private final WeatherMapper weatherMapper;

    @Value("${app.weather.default-lat}")
    private Double defaultLat;

    @Value("${app.weather.default-lon}")
    private Double defaultLon;

    public WeatherScheduler(WeatherService weatherService, WeatherMapper weatherMapper) {
        this.weatherService = weatherService;
        this.weatherMapper = weatherMapper;
    }

    @Scheduled(fixedRateString = "${app.weather.update-rate}")
    public void logWeather() {
        log.info("Scheduled weather update started for lat: {}, lon: {}", defaultLat, defaultLon);
        var weather = weatherService.getWeatherByCoordinateAndDate(defaultLat, defaultLon);
        log.info(
                "Weather updated successfully: Temp = {}, Description = {}, ID = {}",
                weather.getTemperature(),
                weather.getDescription(),
                weather.getId());
    }
}

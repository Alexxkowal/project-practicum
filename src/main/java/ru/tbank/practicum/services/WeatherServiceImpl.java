package ru.tbank.practicum.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.controllers.dto.WeatherDTO;
import ru.tbank.practicum.mappers.WeatherMapper;
import ru.tbank.practicum.repositories.WeatherRepository;
import ru.tbank.practicum.services.dto.WeatherResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherMapper weatherMapper;
    private final WeatherRepository weatherRepository;
    private final WeatherClient weatherClient;

    @Override
    public WeatherDTO getWeatherByCoordinateAndDate(Double lat, Double lon) {
        log.info("Запрос погоды для координат: lat={}, lon={}", lat, lon);
        WeatherResponse weatherResponse = weatherClient.fetchWeather(lat, lon);
        log.info("Погода успешно получена и сохранена в локальный репозиторий");
        return weatherMapper.mapToDto(weatherRepository.save(weatherMapper.mapToModel(weatherResponse)));
    }

    @Override
    public List<WeatherDTO> findAllWeather() {
        return weatherRepository.findAll().stream().map(weatherMapper::mapToDto).toList();
    }
}

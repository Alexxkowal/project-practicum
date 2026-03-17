package ru.tbank.practicum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tbank.practicum.controllers.dto.WeatherDTO;
import ru.tbank.practicum.mappers.WeatherMapper;
import ru.tbank.practicum.repositories.WeatherRepository;
import ru.tbank.practicum.services.dto.WeatherResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

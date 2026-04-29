package ru.tbank.practicum.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.controllers.dto.WeatherDTO;
import ru.tbank.practicum.kafka.dto.WeatherEvent;
import ru.tbank.practicum.kafka.producer.WeatherEventProducer;
import ru.tbank.practicum.mappers.WeatherMapper;
import ru.tbank.practicum.models.WeatherData;
import ru.tbank.practicum.repositories.WeatherRepository;
import ru.tbank.practicum.services.dto.WeatherResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherMapper weatherMapper;
    private final WeatherRepository weatherRepository;
    private final WeatherClient weatherClient;
    private final WeatherEventProducer producer;

    @Override
    public WeatherDTO getWeatherByCoordinateAndDate(Double lat, Double lon) {
        log.info("Запрос погоды для координат: lat={}, lon={}", lat, lon);
        WeatherResponse weatherResponse = weatherClient.fetchWeather(lat, lon);
        WeatherData savedModel = weatherRepository.save(weatherMapper.mapToModel(weatherResponse));
        log.info("Погода сохранена в БД с id={}", savedModel.getId());
        WeatherEvent evt = weatherMapper.mapEntityToEvent(savedModel);
        producer.send(evt);
        return weatherMapper.mapToDto(savedModel);
    }

    @Override
    public List<WeatherDTO> findAllWeather() {
        return weatherRepository.findAll().stream().map(weatherMapper::mapToDto).toList();
    }
}

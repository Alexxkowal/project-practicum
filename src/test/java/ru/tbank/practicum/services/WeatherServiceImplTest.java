package ru.tbank.practicum.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.controllers.dto.WeatherDTO;
import ru.tbank.practicum.kafka.dto.WeatherEvent;
import ru.tbank.practicum.kafka.producer.WeatherEventProducer;
import ru.tbank.practicum.mappers.WeatherMapper;
import ru.tbank.practicum.models.WeatherData;
import ru.tbank.practicum.repositories.WeatherRepository;
import ru.tbank.practicum.services.dto.WeatherResponse;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    @Mock
    private WeatherMapper weatherMapper;

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private WeatherClient weatherClient;

    @Mock
    private WeatherEventProducer producer;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    public void getWeather_ShouldReturnCorrectDto() {
        Double lat = 51.6;
        Double lon = 46.0;

        WeatherResponse mockResponse = new WeatherResponse();
        WeatherData mockEntity = new WeatherData();
        mockEntity.setId(1L);
        WeatherEvent mockEvent = new WeatherEvent("uuid", 20.0, "Sunny", 50, LocalDateTime.now());
        WeatherDTO expectedDto = new WeatherDTO();
        when(weatherClient.fetchWeather(lat, lon)).thenReturn(mockResponse);
        when(weatherMapper.mapToModel(mockResponse)).thenReturn(mockEntity);
        when(weatherRepository.save(mockEntity)).thenReturn(mockEntity);
        when(weatherMapper.mapEntityToEvent(mockEntity)).thenReturn(mockEvent);
        when(weatherMapper.mapToDto(mockEntity)).thenReturn(expectedDto);
        WeatherDTO result = weatherService.getWeatherByCoordinateAndDate(lat, lon);
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(producer, times(1)).send(mockEvent);
        verify(weatherRepository).save(mockEntity);
    }

    @Test
    public void findAllWeather_ShouldReturnDtoList() {
        WeatherData w1 = new WeatherData();
        WeatherData w2 = new WeatherData();
        when(weatherRepository.findAll()).thenReturn(List.of(w1, w2));
        when(weatherMapper.mapToDto(any(WeatherData.class))).thenReturn(new WeatherDTO());
        List<WeatherDTO> result = weatherService.findAllWeather();
        assertEquals(2, result.size());
        verify(weatherMapper, times(2)).mapToDto(any());
    }
}

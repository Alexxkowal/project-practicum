package ru.tbank.practicum.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tbank.practicum.controllers.dto.WeatherDTO;
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

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    public void getWeather_ShouldReturnCorrectDto() {
        Double lat = 55.0, lon = 37.0;
        WeatherResponse mockResponse = new WeatherResponse();
        WeatherData mockEntity = new WeatherData();
        WeatherDTO mockDto = new WeatherDTO();
        mockDto.setTemperature(25.0);
        when(weatherClient.fetchWeather(lat, lon)).thenReturn(mockResponse);
        when(weatherMapper.mapToModel(mockResponse)).thenReturn(mockEntity);
        when(weatherRepository.save(mockEntity)).thenReturn(mockEntity);
        when(weatherMapper.mapToDto(mockEntity)).thenReturn(mockDto);
        WeatherDTO result = weatherService.getWeatherByCoordinateAndDate(lat, lon);
        assertNotNull(result);
        assertEquals(25.0, result.getTemperature());
        verify(weatherClient).fetchWeather(lat, lon);
        verify(weatherRepository).save(any(WeatherData.class));
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

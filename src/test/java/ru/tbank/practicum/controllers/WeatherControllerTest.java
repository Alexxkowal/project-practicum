package ru.tbank.practicum.controllers;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.tbank.practicum.controllers.dto.WeatherDTO;
import ru.tbank.practicum.mappers.WeatherMapper;
import ru.tbank.practicum.services.WeatherService;

@WebMvcTest({WeatherController.class, WeatherMapper.class})
class WeatherControllerTest {

    @MockitoBean
    private WeatherService weatherService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void history_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/weather/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getCurrentWeather() throws Exception {
        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setId(1L);
        weatherDTO.setTemperature(20.0);
        weatherDTO.setDescription("cloudy");

        when(weatherService.getWeatherByCoordinateAndDate(anyDouble(), anyDouble()))
                .thenReturn(weatherDTO);

        mockMvc.perform(get("/weather/current").param("lon", "48.0").param("lat", "51.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.temperature").value(20))
                .andExpect(jsonPath("$.description").value("cloudy"));
    }
}

package ru.tbank.practicum.controllers;

import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;
import ru.tbank.practicum.BaseIT;
import ru.tbank.practicum.repositories.WeatherRepository;

class WeatherControllerTestIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    @DisplayName("Успех: /weather возвращает данные из внешнего API через WireMock")
    void getWeather_Success() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:weather_response.json");
        String responseBody = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathEqualTo("/data/2.5/weather"))
                .willReturn(com.github.tomakehurst.wiremock.client.WireMock.okJson(responseBody)));
        mockMvc.perform(get("/weather/current").param("lat", "51.59").param("lon", "46"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value(277.69))
                .andExpect(jsonPath("$.description").value("light rain"));
        assertEquals(1, weatherRepository.count(), "Запись должна сохраниться в БД");
    }

    @Test
    @DisplayName("Ошибка API: Если WebClient возвращает ошибку, запись в БД не создается")
    void getWeather_WhenApiError_ShouldNotSaveToDb() throws Exception {
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlPathEqualTo("/data/2.5/weather"))
                .willReturn(serverError()));

        mockMvc.perform(get("/weather/current").param("lat", "51.59").param("lon", "46"))
                .andExpect(status().isInternalServerError());
        assertEquals(0, weatherRepository.count(), "Запись не должна была сохраниться при ошибке API");
    }
}

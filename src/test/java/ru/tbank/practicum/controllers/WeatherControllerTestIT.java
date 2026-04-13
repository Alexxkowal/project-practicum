package ru.tbank.practicum.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tbank.practicum.BaseIT;
import ru.tbank.practicum.repositories.WeatherRepository;
import ru.tbank.practicum.services.dto.WeatherResponse;
import ru.tbank.practicum.util.FileUtils;
import tools.jackson.databind.ObjectMapper;

class WeatherControllerTestIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherRepository weatherRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        weatherRepository.deleteAll();
    }

    @Test
    @DisplayName("SpringBootTest: /weather возвращает корректный WeatherDto с моканым WebClient")
    void getWeather_withMockedWebClient_returnsWeatherDto() throws Exception {

        String response = FileUtils.getFileAsString("weather_response.json");
        WeatherResponse weatherResponse = objectMapper.readValue(response, WeatherResponse.class);
        setUpWebClient(weatherResponse);

        mockMvc.perform(get("/weather/current").param("lat", "51.59").param("lon", "46"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value(277.69))
                .andExpect(jsonPath("$.description").value("light rain"));

        assertEquals(1, weatherRepository.count(), "Запись сохранилась в бд");
        var savedWeather = weatherRepository.findAll().getFirst();
        assertEquals(277.69, savedWeather.getTemperature());
    }

    @Test
    @DisplayName("Ошибка API: Если WebClient возвращает ошибку, запись в БД не создается")
    void getWeather_WhenApiError_ShouldNotSaveToDb() throws Exception {
        final var uriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        final var headersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        final var responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(any(java.util.function.Function.class))).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);

        when(responseSpecMock.bodyToMono(WeatherResponse.class))
                .thenReturn(Mono.error(new RuntimeException("External Service Error")));

        mockMvc.perform(get("/weather/current").param("lat", "51.59").param("lon", "46"))
                .andExpect(status().isInternalServerError());
        assertEquals(0, weatherRepository.count(), "Запись не должна была сохраниться при ошибке API");
    }

    private void setUpWebClient(WeatherResponse weatherResponse) {
        final var uriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        final var headersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        final var responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);
        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(any(java.util.function.Function.class))).thenReturn(headersSpecMock);
        when(headersSpecMock.header(any(), any())).thenReturn(headersSpecMock);
        when(headersSpecMock.headers(any())).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(WeatherResponse.class)).thenReturn(Mono.just(weatherResponse));
    }
}

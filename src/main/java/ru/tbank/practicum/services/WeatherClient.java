package ru.tbank.practicum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tbank.practicum.services.dto.WeatherResponse;

@Component
@RequiredArgsConstructor
public class WeatherClient {
    private final WebClient webClient;

    @Value("${app.cred.token}")
    private String token;

    public WeatherResponse fetchWeather(Double lat, Double lon) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/data/2.5/weather")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("units", "metric")
                        .queryParam("appid", token)
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponse.class)
                .block();
    }
}

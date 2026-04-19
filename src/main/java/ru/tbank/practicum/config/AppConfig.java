package ru.tbank.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class AppConfig {

    @Value("${app.weather.base-url:https://api.openweathermap.org}")
    private String weatherBaseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(weatherBaseUrl).build();
    }
}

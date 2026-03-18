package ru.tbank.practicum.controllers;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tbank.practicum.controllers.dto.WeatherDTO;
import ru.tbank.practicum.services.WeatherService;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/history")
    public List<WeatherDTO> history() {
        return weatherService.findAllWeather();
    }

    @GetMapping("/current")
    public WeatherDTO getCurrentWeather(@RequestParam Double lat, @RequestParam Double lon) {
        return weatherService.getWeatherByCoordinateAndDate(lat, lon);
    }
}

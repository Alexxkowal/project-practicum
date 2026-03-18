package ru.tbank.practicum.repositories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.tbank.practicum.models.WeatherData;

@Repository
public class StubWeatherRepository implements WeatherRepository {
    private final Map<Long, WeatherData> weatherDataMap = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<WeatherData> findAll() {
        return new ArrayList<>(weatherDataMap.values());
    }

    public WeatherData save(WeatherData weatherData) {
        if (weatherData.getId() == null) {
            weatherData.setId(idCounter.getAndIncrement());
        }
        weatherDataMap.put(weatherData.getId(), weatherData);
        return weatherData;
    }

    public WeatherData findById(Long id) {
        return weatherDataMap.get(id);
    }

    public List<WeatherData> findByDate(LocalDate date) {
        return weatherDataMap.values().stream()
                .filter(k -> k.getTime().toLocalDate().equals(date))
                .toList();
    }

    public List<WeatherData> findByCoordsAndDate(Double lat, Double lon, LocalDate date) {
        return weatherDataMap.values().stream()
                .filter(w -> w.getLat().equals(lat))
                .filter(w -> w.getLon().equals(lon))
                .filter(w -> w.getTime().toLocalDate().equals(date))
                .toList();
    }
}

package ru.tbank.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tbank.practicum.models.WeatherData;

public interface WeatherRepository extends JpaRepository<WeatherData, Long> {}

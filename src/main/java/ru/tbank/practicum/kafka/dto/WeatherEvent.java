package ru.tbank.practicum.kafka.dto;

import java.time.LocalDateTime;

public record WeatherEvent(
        String eventId,
        Double temperature,
        String description,
        Integer humidity,
        LocalDateTime createdAt
) {
}

package ru.tbank.practicum.services;

import java.time.LocalTime;
import ru.tbank.practicum.models.Blinds;

public interface BlindsService {

    Blinds updatePosition(Long id, Integer position);

    Blinds updateSchedule(Long id, LocalTime openTime, LocalTime closeTime);
}

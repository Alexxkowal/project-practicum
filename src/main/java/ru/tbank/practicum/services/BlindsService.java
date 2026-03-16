package ru.tbank.practicum.services;

import ru.tbank.practicum.controllers.dto.BlindsRequestDTO;
import ru.tbank.practicum.models.Blinds;

import java.time.LocalTime;

public interface BlindsService {

    Blinds updatePosition(Long id, Integer position);

    Blinds updateSchelude(Long id, LocalTime openTime, LocalTime closeTime);

}

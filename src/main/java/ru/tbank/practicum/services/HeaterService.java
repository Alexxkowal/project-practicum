package ru.tbank.practicum.services;

import ru.tbank.practicum.models.Heater;

public interface HeaterService {
    Heater save(Heater heater);

    Heater updateTargetTemperature(Long id, Double targetTemperature);

    Heater updateStatus(Long id, Boolean isWorking);
}

package ru.tbank.practicum.services;

import java.util.List;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.models.Heater;

public interface HeaterService {
    Heater save(Heater heater);

    Heater updateTargetTemperature(Long id, Double targetTemperature);

    Heater updateStatus(Long id, Boolean isWorking);

    void processCommand(DeviceCommand command);

    List<Heater> getAllHeaters();
}

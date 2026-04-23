package ru.tbank.practicum.services;

import java.time.LocalTime;
import java.util.List;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.models.Blinds;

public interface BlindsService {

    Blinds updatePosition(Long id, Integer position);

    Blinds updateSchedule(Long id, LocalTime openTime, LocalTime closeTime);

    void processCommand(DeviceCommand deviceCommand);

    List<Blinds> getAllBlinds();
}

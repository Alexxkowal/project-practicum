package ru.tbank.practicum.services.strategies;

import java.util.List;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.kafka.dto.WeatherEvent;

public interface AutomationStrategy {
    List<DeviceCommand> process(WeatherEvent event);
}

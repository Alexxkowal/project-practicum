package ru.tbank.practicum.services.strategies;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.config.HeaterAutomationConfig;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.kafka.dto.WeatherEvent;
import ru.tbank.practicum.kafka.dto.enums.DeviceAction;
import ru.tbank.practicum.services.HeaterService;

@Component
@RequiredArgsConstructor
public class HeatingStrategy implements AutomationStrategy {

    private final HeaterService heaterService;
    private final HeaterAutomationConfig heaterAutomationConfig;

    @Override
    public List<DeviceCommand> process(WeatherEvent event) {
        double temp = event.temperature();
        return heaterService.getAllHeaters().stream()
                .map(heater -> {
                    if (temp < heaterAutomationConfig.getColdThreshold() && !heater.isWorking()) {
                        return new DeviceCommand("HEATER", heater.getId(), DeviceAction.TURN_ON, 25.0);
                    }
                    if (temp > heaterAutomationConfig.getStopThreshold() && heater.isWorking()) {
                        return new DeviceCommand("HEATER", heater.getId(), DeviceAction.TURN_OFF, 0.0);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

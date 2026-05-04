package ru.tbank.practicum.services.strategies;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.config.HeaterAutomationConfig;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.kafka.dto.WeatherEvent;
import ru.tbank.practicum.kafka.dto.enums.DeviceAction;
import ru.tbank.practicum.models.enums.DeviceType;
import ru.tbank.practicum.services.HeaterService;

@Component
@RequiredArgsConstructor
public class HeatingStrategy implements AutomationStrategy {

    private final HeaterService heaterService;
    private final HeaterAutomationConfig heaterAutomationConfig;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceCommand> process(WeatherEvent event) {
        double temp = event.temperature();
        return heaterService.getAllHeaters().stream()
                .map(heater -> commandForHeater(heater, temp))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private DeviceCommand commandForHeater(Heater heater, double ambientTemp) {
        if (heater.getDevice() == null || !heater.getDevice().isEnabled() || !heater.getDevice().isAutoMode()) {
            return null;
        }
        if (ambientTemp < heaterAutomationConfig.getColdThreshold() && !heater.isWorking()) {
            return new DeviceCommand(DeviceType.HEATER, heater.getId(), DeviceAction.TURN_ON, 25.0);
        }
        if (ambientTemp > heaterAutomationConfig.getStopThreshold() && heater.isWorking()) {
            return new DeviceCommand(DeviceType.HEATER, heater.getId(), DeviceAction.TURN_OFF, 0.0);
        }
        return null;
    }
}

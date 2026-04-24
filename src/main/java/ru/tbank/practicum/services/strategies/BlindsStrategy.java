package ru.tbank.practicum.services.strategies;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tbank.practicum.config.BlindsAutomationConfig;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.kafka.dto.WeatherEvent;
import ru.tbank.practicum.kafka.dto.enums.DeviceAction;
import ru.tbank.practicum.models.Blinds;
import ru.tbank.practicum.models.enums.WeatherCategory;
import ru.tbank.practicum.services.BlindsService;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlindsStrategy implements AutomationStrategy {
    private final BlindsService blindsService;
    private final BlindsAutomationConfig blindsAutomationConfig;

    @Override
    public List<DeviceCommand> process(WeatherEvent event) {
        WeatherCategory category = WeatherCategory.fromCode(event.weatherCode());
        double temp = event.temperature();
        return blindsService.getAllBlinds().stream()
                .map(blinds -> {
                    if (category == WeatherCategory.THUNDERSTORM
                            || event.weatherCode() >= 771 && event.weatherCode() <= 781) {
                        return createCommand(blinds, blindsAutomationConfig.getClosedPosition());
                    }
                    if (category == WeatherCategory.CLEAR) {
                        if (temp > 25.0) return createCommand(blinds, blindsAutomationConfig.getSunnyPosition());
                        if (temp < 5.0) return createCommand(blinds, blindsAutomationConfig.getOpenedPosition());
                    }
                    if (category == WeatherCategory.RAIN || category == WeatherCategory.SNOW) {
                        return (temp < 0)
                                ? createCommand(blinds, blindsAutomationConfig.getRainSnowColdPosition())
                                : createCommand(blinds, blindsAutomationConfig.getRainSnowWarmPosition());
                    }
                    if (category == WeatherCategory.ATMOSPHERE) {
                        return createCommand(blinds, blindsAutomationConfig.getAtmospherePosition());
                    }
                    if (category == WeatherCategory.CLOUDS || category == WeatherCategory.DRIZZLE) {
                        return createCommand(blinds, blindsAutomationConfig.getOpenedPosition());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private DeviceCommand createCommand(Blinds blinds, int pos) {
        log.info(
                "Проверка шторы ID {}: в базе target={}, хотим поставить={}",
                blinds.getId(),
                blinds.getTargetPosition(),
                pos);
        if (blinds.getTargetPosition() == pos) return null;
        return new DeviceCommand("BLINDS", blinds.getId(), DeviceAction.SET_VALUE, (double) pos);
    }
}

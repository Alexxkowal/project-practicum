package ru.tbank.practicum.services.strategies;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.config.BlindsAutomationConfig;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.kafka.dto.WeatherEvent;
import ru.tbank.practicum.kafka.dto.enums.DeviceAction;
import ru.tbank.practicum.models.Blinds;
import ru.tbank.practicum.models.enums.DeviceType;
import ru.tbank.practicum.models.enums.WeatherCategory;
import ru.tbank.practicum.services.BlindsService;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlindsStrategy implements AutomationStrategy {
    private final BlindsService blindsService;
    private final BlindsAutomationConfig blindsAutomationConfig;

    @Override
    @Transactional(readOnly = true)
    public List<DeviceCommand> process(WeatherEvent event) {
        WeatherCategory category = WeatherCategory.fromCode(event.weatherCode());
        int targetPos = calculateTargetPosition(category, event.temperature(), event.weatherCode());

        return blindsService.getAllBlinds().stream()
                .map(blinds -> createCommand(blinds, targetPos))
                .filter(Objects::nonNull)
                .toList();
    }

    private int calculateTargetPosition(WeatherCategory category, double temp, int weatherCode) {
        if (weatherCode >= 771 && weatherCode <= 781) {
            return blindsAutomationConfig.getClosedPosition();
        }
        return switch (category) {
            case THUNDERSTORM -> blindsAutomationConfig.getClosedPosition();
            case CLEAR ->
                (temp > 25.0)
                        ? blindsAutomationConfig.getSunnyPosition()
                        : (temp < 5.0) ? blindsAutomationConfig.getOpenedPosition() : 100;
            case RAIN, SNOW ->
                (temp < 0)
                        ? blindsAutomationConfig.getRainSnowColdPosition()
                        : blindsAutomationConfig.getRainSnowWarmPosition();
            case ATMOSPHERE -> blindsAutomationConfig.getAtmospherePosition();
            case CLOUDS, DRIZZLE -> blindsAutomationConfig.getOpenedPosition();
            default -> 100;
        };
    }

    private DeviceCommand createCommand(Blinds blinds, int pos) {
        if (blinds.getDevice() == null || !blinds.getDevice().isEnabled() || !blinds.getDevice().isAutoMode()) {
            return null;
        }
        log.info(
                "Проверка шторы ID {}: в базе target={}, хотим поставить={}",
                blinds.getId(),
                blinds.getTargetPosition(),
                pos);
        if (blinds.getTargetPosition() == pos) return null;
        return new DeviceCommand(DeviceType.BLINDS, blinds.getId(), DeviceAction.SET_VALUE, (double) pos);
    }
}

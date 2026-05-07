package ru.tbank.practicum.services;

import java.time.Clock;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.kafka.dto.DeviceCommand;
import ru.tbank.practicum.kafka.dto.enums.DeviceAction;
import ru.tbank.practicum.models.Schedule;
import ru.tbank.practicum.models.enums.DeviceType;
import ru.tbank.practicum.models.enums.SchedulesAction;
import ru.tbank.practicum.repositories.ScheduleRepository;

@Slf4j
@Service
@Profile("!test")
@RequiredArgsConstructor
public class ScheduleExecutionService {

    private static final double DEFAULT_HEATER_ON_TEMP = 22.0;

    private final Clock clock;
    private final ScheduleRepository scheduleRepository;
    private final HeaterService heaterService;
    private final BlindsService blindsService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void runDueSchedules() {
        LocalTime now = LocalTime.now(clock).truncatedTo(ChronoUnit.MINUTES);
        for (Schedule schedule : scheduleRepository.findAllActiveWithDevice()) {
            if (!schedule.getExecutionTime().truncatedTo(ChronoUnit.MINUTES).equals(now)) {
                continue;
            }
            if (!schedule.getDevice().isEnabled()) {
                continue;
            }
            DeviceCommand command = toDeviceCommand(schedule);
            if (command == null) {
                log.warn("Расписание {}: не удалось сформировать команду", schedule.getId());
                continue;
            }
            try {
                switch (schedule.getDevice().getType()) {
                    case HEATER -> heaterService.processCommand(command);
                    case BLINDS -> blindsService.processCommand(command);
                }
                log.info(
                        "Расписание {} выполнено для устройства {}",
                        schedule.getId(),
                        schedule.getDevice().getId());
            } catch (Exception e) {
                log.error("Ошибка выполнения расписания {}: {}", schedule.getId(), e.getMessage(), e);
            }
        }
    }

    private DeviceCommand toDeviceCommand(Schedule schedule) {
        Long deviceId = schedule.getDevice().getId();
        DeviceType type = schedule.getDevice().getType();
        return switch (type) {
            case HEATER -> heaterCommand(deviceId, schedule.getAction(), schedule.getTargetValue());
            case BLINDS -> blindsCommand(deviceId, schedule.getAction(), schedule.getTargetValue());
        };
    }

    private DeviceCommand heaterCommand(Long deviceId, SchedulesAction action, Double targetValue) {
        return switch (action) {
            case ON ->
                new DeviceCommand(
                        DeviceType.HEATER,
                        deviceId,
                        DeviceAction.TURN_ON,
                        targetValue != null ? targetValue : DEFAULT_HEATER_ON_TEMP);
            case OFF -> new DeviceCommand(DeviceType.HEATER, deviceId, DeviceAction.TURN_OFF, 0.0);
            case SET_VALUE -> {
                if (targetValue == null) {
                    yield null;
                }
                yield new DeviceCommand(DeviceType.HEATER, deviceId, DeviceAction.SET_VALUE, targetValue);
            }
        };
    }

    private DeviceCommand blindsCommand(Long deviceId, SchedulesAction action, Double targetValue) {
        return switch (action) {
            case ON -> new DeviceCommand(DeviceType.BLINDS, deviceId, DeviceAction.SET_VALUE, 100.0);
            case OFF -> new DeviceCommand(DeviceType.BLINDS, deviceId, DeviceAction.TURN_OFF, 0.0);
            case SET_VALUE -> {
                if (targetValue == null) {
                    yield null;
                }
                yield new DeviceCommand(DeviceType.BLINDS, deviceId, DeviceAction.SET_VALUE, targetValue);
            }
        };
    }
}

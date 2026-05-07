package ru.tbank.practicum.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.practicum.controllers.dto.CreateScheduleRequestDTO;
import ru.tbank.practicum.controllers.dto.SchedulePatchDTO;
import ru.tbank.practicum.controllers.dto.ScheduleResponseDTO;
import ru.tbank.practicum.exception.DeviceNotFoundException;
import ru.tbank.practicum.exception.ScheduleNotFoundException;
import ru.tbank.practicum.models.Device;
import ru.tbank.practicum.models.Schedule;
import ru.tbank.practicum.models.enums.DeviceType;
import ru.tbank.practicum.models.enums.SchedulesAction;
import ru.tbank.practicum.repositories.DeviceRepository;
import ru.tbank.practicum.repositories.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public ScheduleResponseDTO create(Long deviceId, CreateScheduleRequestDTO dto) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException(deviceId));
        Schedule schedule = new Schedule();
        schedule.setDevice(device);
        schedule.setExecutionTime(dto.getExecutionTime());
        schedule.setAction(dto.getAction());
        schedule.setTargetValue(dto.getTargetValue());
        schedule.setActive(dto.getActive() == null || dto.getActive());
        normalizeTargets(schedule);
        validateScheduleRow(device.getType(), schedule.getAction(), schedule.getTargetValue());
        schedule = scheduleRepository.save(schedule);
        return toDto(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponseDTO> listByDevice(Long deviceId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new DeviceNotFoundException(deviceId);
        }
        return scheduleRepository.findAllByDeviceIdOrderByExecutionTimeAsc(deviceId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ScheduleResponseDTO patch(Long scheduleId, SchedulePatchDTO dto) {
        Schedule schedule =
                scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleNotFoundException(scheduleId));
        schedule.getDevice().getId();
        if (dto.getExecutionTime() != null) {
            schedule.setExecutionTime(dto.getExecutionTime());
        }
        if (dto.getAction() != null) {
            schedule.setAction(dto.getAction());
        }
        if (dto.getTargetValue() != null) {
            schedule.setTargetValue(dto.getTargetValue());
        }
        if (dto.getActive() != null) {
            schedule.setActive(dto.getActive());
        }
        normalizeTargets(schedule);
        validateScheduleRow(schedule.getDevice().getType(), schedule.getAction(), schedule.getTargetValue());
        scheduleRepository.save(schedule);
        return toDto(schedule);
    }

    private void normalizeTargets(Schedule schedule) {
        var type = schedule.getDevice().getType();
        var action = schedule.getAction();
        if (type == DeviceType.BLINDS && (action == SchedulesAction.ON || action == SchedulesAction.OFF)) {
            schedule.setTargetValue(null);
        }
        if (type == DeviceType.HEATER && action == SchedulesAction.OFF) {
            schedule.setTargetValue(null);
        }
    }

    @Override
    @Transactional
    public void delete(Long scheduleId) {
        Schedule schedule =
                scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleNotFoundException(scheduleId));
        scheduleRepository.delete(schedule);
    }

    private void validateScheduleRow(DeviceType type, SchedulesAction action, Double targetValue) {
        switch (type) {
            case HEATER -> validateHeaterSchedule(action, targetValue);
            case BLINDS -> validateBlindsSchedule(action, targetValue);
        }
    }

    private static void validateHeaterSchedule(SchedulesAction action, Double targetValue) {
        switch (action) {
            case SET_VALUE -> {
                if (targetValue == null) {
                    throw new IllegalArgumentException("Для обогревателя SET_VALUE нужен targetValue");
                }
                if (targetValue < 5 || targetValue > 35) {
                    throw new IllegalArgumentException("targetValue для обогревателя: 5..35 °C");
                }
            }
            case ON -> {
                if (targetValue != null && (targetValue < 5 || targetValue > 35)) {
                    throw new IllegalArgumentException("targetValue для ON: 5..35 °C");
                }
            }
            case OFF -> {
                if (targetValue != null) {
                    throw new IllegalArgumentException("Для OFF targetValue не используется");
                }
            }
        }
    }

    private static void validateBlindsSchedule(SchedulesAction action, Double targetValue) {
        switch (action) {
            case SET_VALUE -> {
                if (targetValue == null) {
                    throw new IllegalArgumentException("Для жалюзи SET_VALUE нужен targetValue (0..100)");
                }
                if (targetValue < 0 || targetValue > 100) {
                    throw new IllegalArgumentException("Позиция жалюзи: 0..100");
                }
            }
            case ON, OFF -> {
                if (targetValue != null) {
                    throw new IllegalArgumentException("Для жалюзи ON/OFF targetValue не используется");
                }
            }
        }
    }

    private ScheduleResponseDTO toDto(Schedule s) {
        return new ScheduleResponseDTO(
                s.getId(),
                s.getDevice().getId(),
                s.getExecutionTime(),
                s.getAction(),
                s.getTargetValue(),
                s.isActive());
    }
}

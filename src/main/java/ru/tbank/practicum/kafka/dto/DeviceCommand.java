package ru.tbank.practicum.kafka.dto;

import ru.tbank.practicum.kafka.dto.enums.DeviceAction;
import ru.tbank.practicum.models.enums.DeviceType;

public record DeviceCommand(DeviceType deviceType, Long deviceId, DeviceAction action, Double value) {}

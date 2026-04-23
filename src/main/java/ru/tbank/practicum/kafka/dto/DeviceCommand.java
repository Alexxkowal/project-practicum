package ru.tbank.practicum.kafka.dto;

import ru.tbank.practicum.kafka.dto.enums.DeviceAction;

public record DeviceCommand(String deviceType, Long deviceId, DeviceAction action, Double value) {}

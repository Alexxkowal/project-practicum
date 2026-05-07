package ru.tbank.practicum.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.tbank.practicum.models.enums.DeviceStatus;
import ru.tbank.practicum.models.enums.DeviceType;

@Data
@AllArgsConstructor
public class DeviceSummaryResponseDTO {
    private Long id;
    private String name;
    private DeviceType type;
    private DeviceStatus status;
    private boolean autoMode;
    private boolean enabled;
}

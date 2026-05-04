package ru.tbank.practicum.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.tbank.practicum.models.enums.DeviceType;

@Data
public class CreateDeviceRequestDTO {
    @NotBlank
    private String name;

    @NotNull
    private DeviceType type;
}

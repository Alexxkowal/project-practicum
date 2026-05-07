package ru.tbank.practicum.controllers;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.controllers.dto.CreateDeviceRequestDTO;
import ru.tbank.practicum.controllers.dto.DeviceDetailResponseDTO;
import ru.tbank.practicum.controllers.dto.DevicePatchDTO;
import ru.tbank.practicum.controllers.dto.DeviceSummaryResponseDTO;
import ru.tbank.practicum.services.DeviceService;

@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/devices")
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceDetailResponseDTO create(@Valid @RequestBody CreateDeviceRequestDTO dto) {
        return deviceService.create(dto);
    }

    @GetMapping("/devices")
    public List<DeviceSummaryResponseDTO> list() {
        return deviceService.list();
    }

    @GetMapping("/devices/{id}")
    public DeviceDetailResponseDTO get(@PathVariable Long id) {
        return deviceService.getById(id);
    }

    @PatchMapping("/devices/{id}")
    public DeviceDetailResponseDTO patch(@PathVariable Long id, @RequestBody DevicePatchDTO dto) {
        return deviceService.patch(id, dto);
    }

    @DeleteMapping("/devices/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deviceService.delete(id);
    }
}

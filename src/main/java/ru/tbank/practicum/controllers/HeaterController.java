package ru.tbank.practicum.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.controllers.dto.HeaterRequestDTO;
import ru.tbank.practicum.controllers.dto.HeaterResponseDTO;
import ru.tbank.practicum.mappers.HeaterMapper;
import ru.tbank.practicum.models.Heater;
import ru.tbank.practicum.services.HeaterService;

@RestController
@RequestMapping("/heaters")
@RequiredArgsConstructor
public class HeaterController {
    private final HeaterService heaterService;
    private final HeaterMapper heaterMapper;

    @PatchMapping("/{id}/temperature")
    public HeaterResponseDTO setTemperature(@PathVariable("id") Long id, @Valid @RequestBody HeaterRequestDTO dto) {
        Heater heater = heaterService.updateTargetTemperature(id, dto.getTargetTemp());
        return heaterMapper.toResponse(heater);
    }

    @PatchMapping("/{id}/working")
    public HeaterResponseDTO setWorking(@PathVariable("id") Long id, @Valid @RequestBody HeaterRequestDTO dto) {
        Heater heater = heaterService.updateStatus(id, dto.getIsWorking());
        return heaterMapper.toResponse(heater);
    }
}

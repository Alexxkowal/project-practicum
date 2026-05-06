package ru.tbank.practicum.controllers;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tbank.practicum.controllers.dto.CreateScheduleRequestDTO;
import ru.tbank.practicum.controllers.dto.SchedulePatchDTO;
import ru.tbank.practicum.controllers.dto.ScheduleResponseDTO;
import ru.tbank.practicum.services.ScheduleService;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/devices/{deviceId}/schedules")
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponseDTO create(@PathVariable Long deviceId, @Valid @RequestBody CreateScheduleRequestDTO dto) {
        return scheduleService.create(deviceId, dto);
    }

    @GetMapping("/devices/{deviceId}/schedules")
    public List<ScheduleResponseDTO> list(@PathVariable Long deviceId) {
        return scheduleService.listByDevice(deviceId);
    }

    @PatchMapping("/schedules/{scheduleId}")
    public ScheduleResponseDTO patch(@PathVariable Long scheduleId, @RequestBody SchedulePatchDTO dto) {
        return scheduleService.patch(scheduleId, dto);
    }

    @DeleteMapping("/schedules/{scheduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long scheduleId) {
        scheduleService.delete(scheduleId);
    }
}
